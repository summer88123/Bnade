package com.summer.bnade.result.all;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseViewActivity;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.di.ComponentHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends BaseViewActivity<SearchResultContract.Presenter> implements
        SearchResultContract.View, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    SearchResultAdapter mResultAdapter;
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tv_avg_buyout)
    TextView mTvAvgBuyout;
    @BindView(R.id.iv_item_icon)
    ImageView mIvItemIcon;
    @BindView(R.id.chart)
    CombinedChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initToolbar();
        mList.setAdapter(mResultAdapter);
        initChart();

        mPresenter.setData((SearchResultVO) getIntent().getParcelableExtra(Content.EXTRA_DATA));
        mPresenter.load();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mPresenter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mPresenter.filter(newText);
        return true;
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.getXAxis().setEnabled(false);

        mChart.setDrawOrder(new DrawOrder[]{DrawOrder.BAR, DrawOrder.LINE});

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTextColor(Color.WHITE);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setLabelCount(4);
        rightAxis.setTextColor(Color.rgb(60, 220, 78));
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(4);
        leftAxis.setTextColor(Color.rgb(240, 238, 70));
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value > 10000 * 10000) {
                    return (int) (value / 10000 / 10000) + "wG";
                } else {
                    return (int) (value / 10000) + "G";
                }
            }

            @Override
            public int getDecimalDigits() {
                return -1;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_result, menu);

        MenuItem search = menu.findItem(R.id.action_filter);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_result_filter_hint));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void show(SearchResultVO result) {
        mResultAdapter.update(result.getAuctionItems());
        Glide.with(this).load(result.getItem().getUrl()).into(mIvItemIcon);
        mTvAvgBuyout.setText(getString(R.string.result_avg_buyout, getString(R.string.full_gold, result
                .getAvgBuyout() / 10000, result.getAvgBuyout() % 10000 / 100, result.getAvgBuyout() % 100)));
        mChart.setData(result.getCombinedData());
        mChart.invalidate();
        mCollapsingToolbarLayout.setTitle(result.getItem().getName());
    }

    @Override
    protected void injectComponent() {
        DaggerSearchResultComponent.builder().applicationComponent(ComponentHolder.getComponent())
                .searchResultModule(new SearchResultModule(this)).build().inject(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
