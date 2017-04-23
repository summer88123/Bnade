package com.summer.bnade.search;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistorySearchRepo;
import com.summer.bnade.data.error.EmptyDataException;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/13.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {
    private final HistorySearchRepo mHistorySearchRepo;

    private SearchVO mSearchVO;
    private Realm currentRealm;

    @Inject
    SearchPresenter(SearchContract.View view, BnadeRepo repo, HistorySearchRepo historySearchRepo) {
        super(view, repo);
        
        this.mHistorySearchRepo = historySearchRepo;
        mSearchVO = new SearchVO();
    }

    @Override
    public void fuzzySearch(String text) {
        search(text, true);
    }

    @Override
    public void load() {
        mRepo.getHot()
                .map(new Function<List<Hot>, Map<Integer, List<Hot>>>() {
                    @Override
                    public Map<Integer, List<Hot>> apply(@NonNull List<Hot> hots) throws Exception {
                        Map<Integer, List<Hot>> map = new HashMap<>();
                        for (Hot hot : hots) {
                            List<Hot> typeList = map.get(hot.getType());
                            if (typeList == null) {
                                typeList = new ArrayList<>();
                                map.put(hot.getType(), typeList);
                            }
                            typeList.add(hot);
                        }
                        return map;
                    }
                })
                .zipWith(getHistories(), new BiFunction<Map<Integer, List<Hot>>, List<String>, SearchVO>() {
                    @Override
                    public SearchVO apply(@NonNull Map<Integer, List<Hot>> integerListMap, @NonNull List<String>
                            strings) throws Exception {
                        mSearchVO.setMap(integerListMap);
                        mSearchVO.setHistories(strings);
                        mSearchVO.setCurrentType(Hot.MONTH);
                        return mSearchVO;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchVO>() {
                    @Override
                    public void accept(@NonNull SearchVO searchVO) throws Exception {
                        mView.show(searchVO);
                    }
                });
    }

    private Single<List<String>> getHistories() {
        return Single.defer(new Callable<SingleSource<List<String>>>() {
            @Override
            public SingleSource<List<String>> call() throws Exception {
                return Single.just(mHistorySearchRepo.getHistories());
            }
        });
    }

    @Override
    public void search(final String name, final boolean saveHistory) {
        mRepo.getItem(name)
                .doOnSuccess(new Consumer<Item>() {
                    @Override
                    public void accept(@NonNull Item item) throws Exception {
                        if (saveHistory) {
                            mHistorySearchRepo.add(item.getName());
                        }
                    }
                })
                .flatMap(new Function<Item, SingleSource<SearchResultVO>>() {
                    @Override
                    public SingleSource<SearchResultVO> apply(@NonNull Item item) throws Exception {

                        return Single.zip(mRepo.getAuction(item.getId()), Single
                                .just(item), new BiFunction<List<AuctionItem>, Item, SearchResultVO>() {
                            @Override
                            public SearchResultVO apply(@NonNull List<AuctionItem> auctionItems, @NonNull Item item)
                                    throws Exception {
                                return new SearchResultVO(item, auctionItems);
                            }
                        });
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
                        } else {
                            mView.showResult(searchResultVO);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showToast("异常");
                    }
                });
    }

    @Override
    public void selectRealm(Realm realm) {
        this.currentRealm = realm;
    }

    @Override
    public void updateHistory() {
        getHistories()
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
        mSearchVO.setCurrentType(type);
        mView.updateHotSearch(mSearchVO.getHotList());
    }

    @Override
    public void clearHistories() {
        getHistories()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mHistorySearchRepo.clear();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        mView.updateHistories(strings);
                    }
                });
    }
}
