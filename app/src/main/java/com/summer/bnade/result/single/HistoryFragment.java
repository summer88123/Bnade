package com.summer.bnade.result.single;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.home.Provider;
import com.summer.bnade.result.single.entity.AuctionHistoryVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.bnade.utils.Content;
import com.summer.bnade.utils.RxUtil;
import com.summer.bnade.utils.StringHelper;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends BaseFragment {
    @BindView(R.id.tv_one_day)
    TextView mTvOneDay;
    @BindView(R.id.tv_week)
    TextView mTvWeek;
    @BindView(R.id.tv_history)
    TextView mTvHistory;
    @BindView(R.id.tv_day_min)
    TextView mTvDayMin;
    @BindView(R.id.tv_day_max)
    TextView mTvDayMax;
    @BindView(R.id.tv_day_avg)
    TextView mTvDayAvg;
    @BindView(R.id.tv_day_count_avg)
    TextView mTvDayCountAvg;
    @BindView(R.id.tv_week_min)
    TextView mTvWeekMin;
    @BindView(R.id.tv_week_count_avg)
    TextView mTvWeekCountAvg;
    @BindView(R.id.tv_week_avg)
    TextView mTvWeekAvg;
    @BindView(R.id.tv_week_max)
    TextView mTvWeekMax;
    @BindView(R.id.tv_history_min)
    TextView mTvHistoryMin;
    @BindView(R.id.tv_history_count_avg)
    TextView mTvHistoryCountAvg;
    @BindView(R.id.tv_history_avg)
    TextView mTvHistoryAvg;
    @BindView(R.id.tv_history_max)
    TextView mTvHistoryMax;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.chart_one_day)
    CombinedChart mChartOneDay;
    @BindView(R.id.chart_history)
    CombinedChart mChartHistory;
    Item item;
    Realm realm;

    ItemResultContract.Presenter mPresenter;

    public static HistoryFragment getInstance(Item item, Realm realm) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Content.EXTRA_DATA, item);
        bundle.putParcelable(Content.EXTRA_SUB_DATA, realm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Provider) {
            mPresenter = (ItemResultContract.Presenter) ((Provider) context).provide();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(Content.EXTRA_DATA);
            realm = getArguments().getParcelable(Content.EXTRA_SUB_DATA);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Single.just(new Pair<>(item, realm))
                .compose(mPresenter.history())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuctionHistoryVO>() {
                    @Override
                    public void accept(AuctionHistoryVO historyVO) throws Exception {
                        update(historyVO);
                    }
                }, new RxUtil.ContextErrorHandler(getContext()));
    }

    @Override
    public int layout() {
        return R.layout.fragment_history;
    }

    @Override
    public void setUpView() {
        initChart(mChartHistory, "MM-dd");
        initChart(mChartOneDay, "HH:mm");
    }

    public void update(AuctionHistoryVO historyVO) {
        mTvDayMin.setText(StringHelper.fullGold(getContext(), historyVO.getOneDay().getMin()));
        mTvDayMax.setText(StringHelper.fullGold(getContext(), historyVO.getOneDay().getMax()));
        mTvDayAvg.setText(StringHelper.fullGold(getContext(), historyVO.getOneDay().getAvg()));
        mTvDayCountAvg.setText(String.valueOf(historyVO.getOneDay().getAvgCount()));
        mTvWeekMin.setText(StringHelper.fullGold(getContext(), historyVO.getLastWeek().getMin()));
        mTvWeekMax.setText(StringHelper.fullGold(getContext(), historyVO.getLastWeek().getMax()));
        mTvWeekAvg.setText(StringHelper.fullGold(getContext(), historyVO.getLastWeek().getAvg()));
        mTvWeekCountAvg.setText(String.valueOf(historyVO.getLastWeek().getAvgCount()));
        mTvHistoryMin.setText(StringHelper.fullGold(getContext(), historyVO.getHistory().getMin()));
        mTvHistoryMax.setText(StringHelper.fullGold(getContext(), historyVO.getHistory().getMax()));
        mTvHistoryAvg.setText(StringHelper.fullGold(getContext(), historyVO.getHistory().getAvg()));
        mTvHistoryCountAvg.setText(String.valueOf(historyVO.getHistory().getAvgCount()));

        mChartHistory.setData(historyVO.getDataHistory());
        mChartHistory.invalidate();
        mChartOneDay.setData(historyVO.getDataOneDay());
        mChartOneDay.invalidate();
    }

    private void initChart(CombinedChart chart, final String pattern) {
        chart.setScaleYEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});
        chart.setViewPortOffsets(0, 0, 0, 0);

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTextColor(Color.BLACK);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(4);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setTextColor(Color.rgb(60, 220, 78));
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis y = chart.getAxisLeft();
        y.setLabelCount(4);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setValueFormatter(new ChartHelper.GoldAxisFormatter());

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(false);
        x.setValueFormatter(new ChartHelper.TimeAxisFormatter(pattern));
    }
}
