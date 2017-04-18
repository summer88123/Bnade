package com.summer.bnade.token;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.token.entity.WowTokenVO;
import com.summer.bnade.utils.DateUtil;
import com.summer.bnade.utils.DefaultViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WowTokenFragment extends BaseFragment implements WowTokenContract.View, SwipeRefreshLayout
        .OnRefreshListener {
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

    Unbinder unbinder;
    private WowTokenContract.Presenter mPresenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WowTokenFragment.
     */
    public static WowTokenFragment newInstance() {
        WowTokenFragment fragment = new WowTokenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wow_toke, container, false);
        unbinder = ButterKnife.bind(this, view);

        DefaultViewUtil.defaultRefresh(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        initChart();
        initHistoryChart();
        return view;
    }

    private void initHistoryChart() {
        mChartHistory.setViewPortOffsets(0, 0, 0, 0);
        mChartHistory.getDescription().setEnabled(false);
        mChartHistory.setDrawGridBackground(false);
        mChartHistory.setMaxHighlightDistance(300);

        YAxis y = mChartHistory.getAxisRight();
        y.setLabelCount(4, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setGridColor(Color.BLACK);
        y.setAxisLineColor(Color.BLACK);
        y.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) (value / 10000) + "wG";
            }

            @Override
            public int getDecimalDigits() {
                return -1;
            }
        });

        mChartHistory.getAxisLeft().setEnabled(false);
        mChartHistory.getLegend().setEnabled(false);
        mChartHistory.animateXY(2000, 2000);
    }

    private void initChart() {
        mChart.setViewPortOffsets(0, 0, 0, 0);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);

        YAxis y = mChart.getAxisRight();
        y.setLabelCount(4, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setGridColor(Color.BLACK);
        y.setAxisLineColor(Color.BLACK);
        y.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) (value / 10000) + "wG";
            }

            @Override
            public int getDecimalDigits() {
                return -1;
            }
        });

        mChart.getAxisLeft().setEnabled(false);
        mChart.getLegend().setEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.load();
    }

    @Override
    public void setPresenter(WowTokenContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showCurrentToken(WowTokenVO current) {
        mTvCurPrice.setText(getString(R.string.gold, current.getCurrentGold()));
        mTvModifiedTime.setText(DateUtil.format(current.getLastModified(), "M月d日 H:m:s"));
        mTvMinPrice.setText(getString(R.string.gold, current.getMinGold()));
        mTvMaxPrice.setText(getString(R.string.gold, current.getMaxGold()));
    }

    @Override
    public void showHistoryChart(List<Entry> allTokens) {
        LineDataSet set1;

        if (mChartHistory.getData() != null &&
                mChartHistory.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChartHistory.getData().getDataSetByIndex(0);
            set1.setValues(allTokens);
            mChartHistory.getData().notifyDataChanged();
            mChartHistory.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(allTokens, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(2.4f);
            set1.setHighlightEnabled(false);
            set1.setColor(Color.BLUE);
            set1.setFillColor(Color.BLUE);
            set1.setFillAlpha(50);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data1 = new LineData(set1);
            data1.setValueTextSize(9f);
            data1.setDrawValues(false);

            // set data
            mChartHistory.setData(data1);
        }
        mChartHistory.animateXY(2000, 2000);
    }

    @Override
    public void showOneDayChart(List<Entry> data) {
        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(data);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(data, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(2.4f);
            set1.setHighlightEnabled(false);
            set1.setColor(Color.BLUE);
            set1.setFillColor(Color.BLUE);
            set1.setFillAlpha(50);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data1 = new LineData(set1);
            data1.setValueTextSize(9f);
            data1.setDrawValues(false);

            // set data
            mChart.setData(data1);
        }
        mChart.animateXY(2000, 2000);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mPresenter.load();
    }

}
