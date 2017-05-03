package com.summer.bnade.token;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.token.entity.WowTokenVO;
import com.summer.bnade.utils.DateUtil;
import com.summer.bnade.utils.DefaultViewUtil;
import com.summer.bnade.utils.ScreenUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WowTokenFragment extends BaseFragment<WowTokenContract.Presenter> implements WowTokenContract.View,
        SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = WowTokenFragment.class.getSimpleName();
    private static final NumberFormat format = new DecimalFormat("0.#");
    @BindView(R.id.tv_cur_price)
    TextView mTvCurPrice;
    @BindView(R.id.tv_modified_time)
    TextView mTvModifiedTime;
    @BindView(R.id.tv_min_price)
    TextView mTvMinPrice;
    @BindView(R.id.tv_max_price)
    TextView mTvMaxPrice;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.chart_one_day)
    LineChart mChart;
    @BindView(R.id.chart_history)
    LineChart mChartHistory;

    Unbinder unbinder;
    @BindView(R.id.cardView1)
    CardView mCardView1;
    @BindView(R.id.cardView2)
    CardView mCardView2;
    @BindView(R.id.cardView3)
    CardView mCardView3;
    @BindColor(R.color.pve_label)
    int blue;

    @BindViews({R.id.cardView1, R.id.cardView2, R.id.cardView3})
    List<CardView> mCardViewList;

    ButterKnife.Setter<CardView, Float> endState = new ButterKnife.Setter<CardView, Float>() {
        @Override
        public void set(@NonNull CardView view, Float value, int index) {
            view.setAlpha(1f);
            view.setTranslationY(value);
        }
    };

    public static WowTokenFragment getInstance(FragmentManager fm) {
        WowTokenFragment fragment = (WowTokenFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new WowTokenFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wow_toke, container, false);
        unbinder = ButterKnife.bind(this, view);

        DefaultViewUtil.defaultRefresh(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        setupChart(mChart, "HH:mm");
        setupChart(mChartHistory, "MM-dd");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideCardView();
        mPresenter.load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showCurrentToken(WowTokenVO current) {
        mTvCurPrice.setText(getString(R.string.gold, current.getCurrentGold()));
        mTvModifiedTime.setText(DateUtil.format(current.getLastModified(), "M月d日 H:mm"));
        mTvMinPrice.setText(getString(R.string.gold, current.getMinGold()));
        mTvMaxPrice.setText(getString(R.string.gold, current.getMaxGold()));
        animateIn(mCardView1, 0);
    }

    @Override
    public void showHistoryChart(List<Entry> allTokens) {
        setupChartData(allTokens, mChartHistory);
        mChartHistory.animateXY(1500, 1500);
        animateIn(mCardView3, 300);
    }

    @Override
    public void showOneDayChart(List<Entry> data) {
        setupChartData(data, mChart);
        mChart.animateXY(1000, 1000);
        animateIn(mCardView2, 150);
    }

    @Override
    public void refreshOver() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshStart() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        animateOut(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideCardView();
                mChartHistory.setScaleX(1);
                mChart.setScaleX(1);
                mPresenter.load();
            }
        });
    }

    private void animateIn(CardView cardView, int startDelay) {
        cardView.animate()
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .setStartDelay(startDelay)
                .start();
    }

    private void animateOut(AnimatorListenerAdapter listener) {
        AnimatorSet as = new AnimatorSet();
        as.addListener(listener);
        View[] views = {mCardView1, mCardView2, mCardView3};
        ObjectAnimator[] animators = new ObjectAnimator[3];
        for (int i = 0; i < views.length; i++) {
            animators[i] = ObjectAnimator.ofFloat(views[i], "alpha", 0f);
        }
        as.setDuration(500).playTogether((Animator[]) animators);
        as.start();
    }

    private void hideCardView() {
        float screenHeight = ScreenUtil.getScreenHeight(getContext());
        ButterKnife.apply(mCardViewList, endState, screenHeight);
    }

    private void setupChart(LineChart chart, final String pattern) {
        chart.setScaleYEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setViewPortOffsets(0, 0, 0, 0);

        YAxis y = chart.getAxisRight();
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
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

    private void setupChartData(List<Entry> data, LineChart chart) {
        LineDataSet set;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(data);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(data,"");

            set.setMode(LineDataSet.Mode.LINEAR);
            set.setDrawFilled(true);
            set.setDrawCircles(false);
            set.setLineWidth(1.4f);
            set.setHighlightEnabled(false);
            set.setColor(blue);
            set.setFillColor(blue);
            set.setFillAlpha(30);

            // create a data object with the datasets
            LineData data1 = new LineData(set);
            data1.setValueTextSize(9f);
            data1.setDrawValues(false);

            // set data
            chart.setData(data1);
        }
    }

}
