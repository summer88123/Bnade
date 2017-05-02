package com.summer.bnade.result;

import android.graphics.Color;
import android.text.TextUtils;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.lib.model.entity.AuctionItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/14.
 */

class SearchResultPresenter extends BasePresenter<SearchResultContract.View> implements SearchResultContract.Presenter {

    private SearchResultVO mResult;

    private List<AuctionItem> mRealData;

    @Inject
    SearchResultPresenter(SearchResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void filter(final String query) {
        Observable.fromIterable(mRealData)
                .filter(new Predicate<AuctionItem>() {
                    @Override
                    public boolean test(@NonNull AuctionItem auctionItem) throws Exception {
                        return TextUtils.isEmpty(query) || auctionItem.getRealm().getConnected().contains(query);
                    }
                })
                .toList()
                .map(new Function<List<AuctionItem>, SearchResultVO>() {
                    @Override
                    public SearchResultVO apply(@NonNull List<AuctionItem> auctionItems) throws Exception {
                        mResult.setAuctionItems(auctionItems);
                        return mResult;
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
    public void setData(SearchResultVO data) {
        this.mResult = data;
        this.mRealData = mResult.getAuctionItems();
    }

    @Override
    public void load() {
        Single.just(mResult)
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

                                data.setData(generateLineData(searchResultVO.getAuctionItems()));
                                data.setData(generateBarData(searchResultVO.getAuctionItems()));
                                searchResultVO.setCombinedData(data);
                                BigDecimal sum = new BigDecimal(0);
                                for (AuctionItem auctionItem : searchResultVO.getAuctionItems()) {
                                    sum = sum.add(new BigDecimal(auctionItem.getMinBuyOut()));
                                }
                                searchResultVO.setAvgBuyout(sum.divide(new BigDecimal(searchResultVO.getAuctionItems()
                                        .size()), 0, BigDecimal.ROUND_CEILING)
                                        .longValue());
                                return searchResultVO;
                            }
                        })
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private LineData generateLineData(List<AuctionItem> items) {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0, size = items.size(); i < size; i++) {
            AuctionItem item = items.get(i);
            entries.add(new Entry(i, item.getMinBuyOut(), item));
        }

        LineDataSet set = new LineDataSet(entries, "价格");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(1.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(2f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(List<AuctionItem> items) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0, size = items.size(); i < size; i++) {
            AuctionItem item = items.get(i);
            entries.add(new BarEntry(i, item.getTotal(), item));
        }

        BarDataSet set = new BarDataSet(entries, "数量");
        set.setColor(Color.rgb(60, 220, 78));
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return new BarData(set);
    }
}
