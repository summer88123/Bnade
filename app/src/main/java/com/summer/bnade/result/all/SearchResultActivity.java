package com.summer.bnade.result.all;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseActivity;
import com.summer.bnade.utils.ChartHelper;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.Item;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import butterknife.BindView;

public class SearchResultActivity extends BaseActivity<ResultUIModel> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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

    SearchView mSearchView;

    @Inject
    SearchResultAdapter mResultAdapter;
    @Inject
    SearchResultTransformer mPresenter;

    Item item;

    @Override
    public int layout() {
        return R.layout.activity_search_result;
    }

    @Override
    public void setUpView() {
        initToolbar();
        mList.setAdapter(mResultAdapter);
        initChart();
        Glide.with(SearchResultActivity.this).load(item.getUrl()).into(mIvItemIcon);
        mCollapsingToolbarLayout.setTitle(item.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_result, menu);
        MenuItem search = menu.findItem(R.id.action_filter);
        mSearchView = (SearchView) search.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_result_filter_hint));
        RxSearchView.queryTextChangeEvents(mSearchView)
                .compose(mPresenter.filter())
                .compose(bindToLifecycle())
                .subscribe(mResultAdapter::update);
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
    public void setUpObservable() {
        untilEmit(ActivityEvent.START)
                .map(ignore -> item)
                .compose(mPresenter.list())
                .compose(bindToLifecycle())
                .subscribe(showAs());
    }

    @Override
    protected void onSuccess(ResultUIModel model) {
        mResultAdapter.update(model.getAuctionItems());
        Gold avgBuyout = model.getAvg();
        mTvAvgBuyout.setText(getString(R.string.result_avg_buyout,
                getString(R.string.full_gold, avgBuyout.getGold(), avgBuyout.getSilver(), avgBuyout
                        .getCopper())));
        mChart.setData(ChartHelper
                .generateCombinedData(SearchResultActivity.this, model.getLines(), model.getBars()));
        mChart.animateXY(1000, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        item = getIntent().getParcelableExtra(Content.EXTRA_DATA);
        super.onCreate(savedInstanceState);
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
        rightAxis.setTextColor(ContextCompat.getColor(this, R.color.uncommon));
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(4);
        leftAxis.setTextColor(ContextCompat.getColor(this, R.color.artifact));
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new ChartHelper.GoldAxisFormatter());
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle("");
    }
}
