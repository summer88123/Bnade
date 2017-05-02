package com.summer.bnade.search;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.data.HistorySearchRepo;
import com.summer.bnade.data.error.EmptyDataException;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/13.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {
    private final HistorySearchRepo mHistorySearchRepo;
    private final HistoryRealmRepo mRealmRepo;

    @Inject
    SearchPresenter(SearchContract.View view, BnadeRepo repo, HistorySearchRepo historySearchRepo,
                    HistoryRealmRepo realmRepo) {
        super(view, repo);
        this.mHistorySearchRepo = historySearchRepo;
        this.mRealmRepo = realmRepo;
    }

    @Override
    public void load(int hotType) {
        Single.zip(mRepo.getHot(hotType), mHistorySearchRepo.getHistories().toList(),
                new BiFunction<List<Hot>, List<String>, SearchVO>() {
                    @Override
                    public SearchVO apply(@NonNull List<Hot> hotlist, @NonNull List<String>
                            strings) throws Exception {
                        return new SearchVO(hotlist, strings);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchVO>() {
                    @Override
                    public void accept(@NonNull SearchVO searchVO) throws Exception {
                        mView.show(searchVO);
                    }
                }, mErrorHandler);
    }

    @Override
    public void search(final String name, final Realm realm) {
        mRepo.getItem(name)
                .flatMap(new Function<Item, SingleSource<Item>>() {
                    @Override
                    public SingleSource<Item> apply(@NonNull Item item) throws Exception {
                        Single<Item> temp = mHistorySearchRepo.add(item.getName()).andThen(Single.just(item));
                        if (realm != null) {
                            temp = mRealmRepo.add(realm).andThen(temp);
                        }
                        return temp;
                    }
                })
                .flatMap(new Function<Item, SingleSource<SearchResultVO>>() {
                    @Override
                    public SingleSource<SearchResultVO> apply(@NonNull Item item) throws Exception {
                        if (realm != null) {
                            return Single.zip(mRepo.getAuctionRealmItem(realm.getId(), item.getId()), Single
                                            .just(item),
                                    new BiFunction<List<AuctionRealmItem>, Item, SearchResultVO>() {
                                        @Override
                                        public SearchResultVO apply(@NonNull List<AuctionRealmItem> strings, @NonNull
                                                Item item) throws Exception {
                                            SearchResultVO result = new SearchResultVO(item);
                                            result.setAuctionRealmItems(strings);
                                            return result;
                                        }
                                    });
                        } else {
                            return Single.zip(mRepo.getAuction(item.getId()), Single
                                    .just(item), new BiFunction<List<AuctionItem>, Item, SearchResultVO>() {
                                @Override
                                public SearchResultVO apply(@NonNull List<AuctionItem> auctionItems, @NonNull Item item)
                                        throws Exception {
                                    return new SearchResultVO(item, auctionItems);
                                }
                            });
                        }
                    }
                })
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends SearchResultVO>>() {
                    @Override
                    public SingleSource<? extends SearchResultVO> apply(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof EmptyDataException) {
                            return mRepo.getItemNames(name).map(new Function<List<String>, SearchResultVO>() {
                                @Override
                                public SearchResultVO apply(@NonNull List<String> strings) throws Exception {
                                    return new SearchResultVO(strings);
                                }
                            });
                        }
                        return Single.error(throwable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResultVO>() {

                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        if (searchResultVO.getItem() == null) {
                            mView.showFuzzySearch(searchResultVO);
                        } else if (searchResultVO.getAuctionRealmItems() != null) {
                            if (searchResultVO.getAuctionRealmItems().isEmpty()) {
                                mView.showToast("无拍卖数据");
                            } else {
                                mView.showRealmItemResult(searchResultVO);
                            }
                        } else {
                            mView.showResult(searchResultVO);
                        }
                    }
                }, mErrorHandler);
    }

    @Override
    public void updateHistory() {
        mHistorySearchRepo.getHistories().toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        mView.updateHistories(strings);
                    }
                });
    }

    @Override
    public void updateHotSearchType(int type) {
        mRepo.getHot(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Hot>>() {
                    @Override
                    public void accept(@NonNull List<Hot> hots) throws Exception {
                        mView.updateHotSearch(hots);
                    }
                }, mErrorHandler);
    }

    @Override
    public void clearHistories() {
        mHistorySearchRepo.clear()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.updateHistories(Collections.<String>emptyList());
                    }
                });
    }
}
