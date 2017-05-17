package com.summer.bnade.utils;

import android.graphics.Color;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.BiFunction;

/**
 * Created by kevin.bai on 2017/5/6.
 */

public class ChartHelper {
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
