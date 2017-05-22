package com.summer.bnade.token;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.home.MainComponent;
import com.summer.bnade.home.Provider;
import com.summer.bnade.token.entity.WowTokenVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.bnade.utils.DateUtil;
import com.summer.bnade.utils.DefaultViewUtil;
import com.summer.bnade.utils.ScreenUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class WowTokenFragment extends BaseFragment {
    public static final String TAG = WowTokenFragment.class.getSimpleName();
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

    @BindView(R.id.cardView1)
    CardView mCardView1;
    @BindView(R.id.cardView2)
    CardView mCardView2;
    @BindView(R.id.cardView3)
    CardView mCardView3;
    @BindColor(R.color.wow_token)
    int blue;
    @BindColor(R.color.gold)
    int gold;

    @BindViews({R.id.cardView1, R.id.cardView2, R.id.cardView3})
    List<CardView> mCardViewList;

    ButterKnife.Setter<CardView, Float> endState = (view, value, index) -> {
        view.setAlpha(1f);
        view.setTranslationY(value);
    };

    @Inject
    WowTokenTransformer mPresenter;

    public static WowTokenFragment getInstance(FragmentManager fm) {
        WowTokenFragment fragment = (WowTokenFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new WowTokenFragment();
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Provider) {
            MainComponent component = (MainComponent) ((Provider) context).provide();
            component.inject(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        hideCardView();
        Observable.just(new Object())
                .compose(mPresenter.load())
                .subscribe(this::show);
    }

    public void show(WowTokenVO current) {
        mRefreshLayout.setRefreshing(current.isInProgress());
        if (!current.isInProgress())
            if (current.isSuccess()) {
                mTvCurPrice.setText(getString(R.string.gold, current.getCurrentGold()));
                mTvModifiedTime.setText(DateUtil.format(current.getLastModified(), "M月d日 H:mm"));
                mTvMinPrice.setText(getString(R.string.gold, current.getMinGold()));
                mTvMaxPrice.setText(getString(R.string.gold, current.getMaxGold()));
                animateIn(mCardView1, 0);
                showOneDayChart(current.getOneDayTokens());
                showHistoryChart(current.getAllTokens());
            } else {
                showToast(current.getErrorMsg());
            }
    }

    public void showHistoryChart(List<Entry> allTokens) {
        setupChartData(allTokens, mChartHistory);
        mChartHistory.animateXY(1500, 1500);
        animateIn(mCardView3, 300);
    }

    public void showOneDayChart(List<Entry> data) {
        setupChartData(data, mChart);
        mChart.animateXY(1000, 1000);
        animateIn(mCardView2, 150);
    }

    @Override
    public int layout() {
        return R.layout.fragment_wow_toke;
    }

    @Override
    public void setUpView() {
        DefaultViewUtil.defaultRefresh(mRefreshLayout);
        RxSwipeRefreshLayout.refreshes(mRefreshLayout)
                .doOnNext(o -> animateOut(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        hideCardView();
                        mChartHistory.setScaleX(1);
                        mChart.setScaleX(1);
                    }
                }))
                .compose(mPresenter.load())
                .subscribe(this::show);
        setupChart(mChart, "HH:mm");
        setupChart(mChartHistory, "MM-dd");
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
        y.setTextColor(gold);
        y.setValueFormatter(new ChartHelper.GoldAxisFormatter(true));

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);
        x.setValueFormatter(new ChartHelper.TimeAxisFormatter(pattern));

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
            set = new LineDataSet(data, "");

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
