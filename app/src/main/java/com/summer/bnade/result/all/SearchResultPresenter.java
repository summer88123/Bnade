package com.summer.bnade.result.all;

import android.text.TextUtils;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.summer.bnade.utils.ChartHelper.generateBarData;

/**
 * Created by kevin.bai on 2017/4/14.
 */

class SearchResultPresenter extends BasePresenter<SearchResultContract.View> implements SearchResultContract.Presenter {

    @Inject
    SearchResultPresenter(SearchResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void filter(final String query, Item item, Realm realm) {
        mRepo.search(item, realm)
                .flatMap(new Function<SearchResultVO, SingleSource<SearchResultVO>>() {
                    @Override
                    public SingleSource<SearchResultVO> apply(@NonNull final SearchResultVO resultVO) throws Exception {
                        return Observable.fromIterable(resultVO.getAuctionItems())
                                .filter(new Predicate<AuctionItem>() {
                                    @Override
                                    public boolean test(@NonNull AuctionItem auctionItem) throws Exception {
                                        return TextUtils.isEmpty(query) || auctionItem.getRealm().getConnected()
                                                .contains(query);
                                    }
                                })
                                .toList()
                                .map(new Function<List<AuctionItem>, SearchResultVO>() {
                                    @Override
                                    public SearchResultVO apply(@NonNull List<AuctionItem> auctionItems) throws
                                            Exception {
                                        resultVO.setAuctionItems(auctionItems);
                                        return resultVO;
                                    }
                                });
                    }
                })
                .compose(computeData())
                .subscribe(new Consumer<SearchResultVO>() {
                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        mView.show(searchResultVO);
                    }
                }, mErrorHandler);

    }

    @Override
    public void load(Item item, Realm realm) {
        mRepo.search(item, realm)
                .compose(computeData())
                .subscribe(new Consumer<SearchResultVO>() {
                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        mView.show(searchResultVO);
                    }
                });

    }

    private SingleTransformer<SearchResultVO, SearchResultVO> computeData() {
        return new SingleTransformer<SearchResultVO, SearchResultVO>() {
            @Override
            public SingleSource<SearchResultVO> apply(@NonNull Single<SearchResultVO> upstream) {
                return upstream
                        .map(new Function<SearchResultVO, SearchResultVO>() {
                            @Override
                            public SearchResultVO apply(@NonNull SearchResultVO searchResultVO) throws Exception {
                                CombinedData data = new CombinedData();

                                data.setData(ChartHelper.generateLineData(searchResultVO
                                        .getAuctionItems(), new BiFunction<Integer, AuctionItem, Entry>() {
                                    @Override
                                    public Entry apply(Integer index, @NonNull AuctionItem auctionItem) throws
                                            Exception {
                                        return new Entry(index, auctionItem.getMinBuyOut().getMoney(), auctionItem);
                                    }
                                }));
                                data.setData(generateBarData(searchResultVO
                                        .getAuctionItems(), new BiFunction<Integer, AuctionItem, BarEntry>() {
                                    @Override
                                    public BarEntry apply(@NonNull Integer integer, @NonNull AuctionItem auctionItem)
                                            throws Exception {
                                        return new BarEntry(integer, auctionItem.getTotal(), auctionItem);
                                    }
                                }));
                                searchResultVO.setCombinedData(data);

                                BigDecimal sum = new BigDecimal(0);
                                for (AuctionItem auctionItem : searchResultVO.getAuctionItems()) {
                                    sum = sum.add(new BigDecimal(auctionItem.getMinBuyOut().getMoney()));
                                }
                                searchResultVO.setAvgBuyout(new Gold(sum
                                        .divide(new BigDecimal(searchResultVO.getAuctionItems()
                                                .size()), 0, BigDecimal.ROUND_CEILING).longValue()));
                                return searchResultVO;
                            }
                        })
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
