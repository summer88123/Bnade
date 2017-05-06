package com.summer.bnade.result.single;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.summer.bnade.R;
import com.summer.bnade.search.entity.SearchResultVO;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends PageAdapter.PageFragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();
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
    Unbinder unbinder;

    public static HistoryFragment getInstance(FragmentManager fm) {
        HistoryFragment fragment = (HistoryFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new HistoryFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    int title() {
        return R.string.fragment_title_history_trand;
    }

    public void update(SearchResultVO resultVO) {
        mTvDayMin.setText(getString(R.string.gold, 15));
        mTvDayMax.setText(getString(R.string.gold, 19));
        mTvDayAvg.setText(getString(R.string.gold, 19));
        mTvDayCountAvg.setText("1111");
        mTvWeekMin.setText(getString(R.string.gold, 19));
        mTvWeekMax.setText(getString(R.string.gold, 19));
        mTvWeekAvg.setText(getString(R.string.gold, 19));
        mTvWeekCountAvg.setText("1112");
        mTvHistoryMin.setText(getString(R.string.gold, 19));
        mTvHistoryMax.setText(getString(R.string.gold, 19));
        mTvHistoryAvg.setText(getString(R.string.gold, 19));
        mTvHistoryCountAvg.setText("1123");

        mChartHistory.setData(resultVO.getCombinedData());
        mChartHistory.invalidate();
        mChartOneDay.setData(resultVO.getCombinedData());
        mChartOneDay.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
