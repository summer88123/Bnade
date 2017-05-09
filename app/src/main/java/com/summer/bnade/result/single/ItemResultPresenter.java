package com.summer.bnade.result.single;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.AuctionHistory;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/28.
 */

class ItemResultPresenter extends BasePresenter<ItemResultContract.View> implements
        ItemResultContract.Presenter {

    private SearchResultVO mResultVO;

    @Inject
    ItemResultPresenter(ItemResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void setData(SearchResultVO data) {
        this.mResultVO = data;
    }

    @Override
    public void load() {
        Single.just(mResultVO)
                .map(new Function<SearchResultVO, SearchResultVO>() {
                    @Override
                    public SearchResultVO apply(@NonNull SearchResultVO resultVO) throws Exception {
                        CombinedData data = new CombinedData();

                        data.setData(ChartHelper.generateLineData(resultVO
                                .getAuctionHistories(), new BiFunction<Integer, AuctionHistory, Entry>() {
                            @Override
                            public Entry apply(Integer index, @NonNull AuctionHistory auctionItem) throws
                                    Exception {
                                return new Entry(index, auctionItem.getMinBuyout().getMoney(), auctionItem);
                            }
                        }));
                        data.setData(ChartHelper.generateBarData(resultVO
                                .getAuctionHistories(), new BiFunction<Integer, AuctionHistory, BarEntry>() {
                            @Override
                            public BarEntry apply(@NonNull Integer integer, @NonNull AuctionHistory auctionItem)
                                    throws Exception {
                                return new BarEntry(integer, auctionItem.getCount(), auctionItem);
                            }
                        }));
                        resultVO.setCombinedData(data);
                        return resultVO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResultVO>() {
                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        mView.show(searchResultVO);
                    }
                }, mErrorHandler);
    }
}
