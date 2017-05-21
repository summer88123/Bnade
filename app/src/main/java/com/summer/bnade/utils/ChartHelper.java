package com.summer.bnade.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.summer.bnade.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.BiFunction;

/**
 * Created by kevin.bai on 2017/5/6.
 */

public class ChartHelper {
    private static final String TAG = ChartHelper.class.getSimpleName();
    private static final NumberFormat format = new DecimalFormat("0.0");

    public static <T> LineData generateLineData(List<T> items, BiFunction<Integer, T, Entry> convert) {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0, size = items.size(); i < size; i++) {
            try {
                entries.add(convert.apply(i, items.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public static <T, D> List<D> generateData(List<T> items, BiFunction<Integer, T, D> convert) {
        List<D> data = new ArrayList<>(items.size());
        for (int i = 0, size = items.size(); i < size; i++) {
            try {
                data.add(convert.apply(i, items.get(i)));
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage(), ignored);
            }
        }
        return data;
    }

    public static CombinedData generateCombinedData(Context context, List<Entry> lines, List<BarEntry> bars) {
        CombinedData data = new CombinedData();

        BarDataSet barSet = new BarDataSet(bars, "数量");
        barSet.setColor(ContextCompat.getColor(context, R.color.uncommon));
        barSet.setDrawValues(false);
        barSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        BarData barData = new BarData(barSet);

        LineDataSet lineSet = new LineDataSet(lines, "价格");
        int color = ContextCompat.getColor(context, R.color.artifact);
        lineSet.setColor(color);
        lineSet.setLineWidth(1.5f);
        lineSet.setCircleColor(color);
        lineSet.setCircleRadius(2f);
        lineSet.setFillColor(color);
        lineSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineSet.setDrawValues(false);
        lineSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData(lineSet);

        data.setData(lineData);
        data.setData(barData);
        return data;
    }

    public static <T> BarData generateBarData(List<T> items, BiFunction<Integer, T, BarEntry> convert) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0, size = items.size(); i < size; i++) {
            try {
                entries.add(convert.apply(i, items.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BarDataSet set = new BarDataSet(entries, "数量");
        set.setColor(Color.rgb(60, 220, 78));
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return new BarData(set);
    }

    public static class GoldAxisFormatter implements IAxisValueFormatter {
        private final int unit;

        public GoldAxisFormatter() {
            this(false);
        }

        public GoldAxisFormatter(boolean isGoldUnit) {
            unit = isGoldUnit ? 1 : 10000;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value > 10000 * unit) {
                return format.format(value / 10000 / unit) + "wG";
            } else {
                return format.format(value / unit) + "G";
            }
        }

        @Override
        public int getDecimalDigits() {
            return -1;
        }
    }

    public static class TimeAxisFormatter implements IAxisValueFormatter {
        private final String pattern;

        public TimeAxisFormatter(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return DateUtil.format((long) value, pattern);
        }

        @Override
        public int getDecimalDigits() {
            return -1;
        }
    }
}
