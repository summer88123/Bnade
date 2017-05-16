package com.summer.bnade.result.single;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.result.single.entity.AuctionHistoryVO;
import com.summer.bnade.utils.DateUtil;
import com.summer.bnade.utils.StringHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends BaseFragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();
    private static final NumberFormat format = new DecimalFormat("0.0");
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

    public static HistoryFragment getInstance(FragmentManager fm) {
        HistoryFragment fragment = (HistoryFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new HistoryFragment();
        }
        return fragment;
    }

    @Override
    public int title() {
        return R.string.fragment_title_history_trand;
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
        chart.getAxisLeft().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setViewPortOffsets(0, 0, 0, 0);

        YAxis y = chart.getAxisLeft();
        y.setLabelCount(4);
        y.setDrawGridLines(false);
        y.setDrawAxisLine(false);
        y.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return format.format(value / 10000) + "wG";
            }

            @Override
            public int getDecimalDigits() {
                return -1;
            }
        });

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setLabelCount(4);
        rightAxis.setTextColor(Color.rgb(60, 220, 78));
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DateUtil.format((long) value, pattern);
            }

            @Override
            public int getDecimalDigits() {
                return -1;
            }
        });

    }
}
