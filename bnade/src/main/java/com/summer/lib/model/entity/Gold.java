package com.summer.lib.model.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.summer.lib.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class Gold implements Parcelable, Comparable<Gold> {
    public static final Parcelable.Creator<Gold> CREATOR = new Parcelable.Creator<Gold>() {
        @Override
        public Gold createFromParcel(Parcel source) {
            return new Gold(source);
        }

        @Override
        public Gold[] newArray(int size) {
            return new Gold[size];
        }
    };
    private static final NumberFormat format = new DecimalFormat("00");
    private static final NumberFormat goldFormat = new DecimalFormat("0");

    private static ImageSpan goldSpan;
    private static ImageSpan silverSpan;
    private static ImageSpan copperSpan;
    private static ForegroundColorSpan goldColorSpan;
    private static ForegroundColorSpan silverColorSpan;
    private static ForegroundColorSpan copperColorSpan;

    private static void initSpan(Context context) {
        if (goldSpan == null || silverSpan == null || copperSpan == null || goldColorSpan == null || silverColorSpan
                == null || copperColorSpan == null) {
            goldSpan = new ImageSpan(context, R.mipmap.gold, ImageSpan.ALIGN_BASELINE);
            goldColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gold));
            silverSpan = new ImageSpan(context, R.mipmap.silver, ImageSpan.ALIGN_BASELINE);
            silverColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.silver));
            copperSpan = new ImageSpan(context, R.mipmap.copper, ImageSpan.ALIGN_BASELINE);
            copperColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.copper));
        }
    }

    private final long money;
    private long gold = -1;
    private int silver = -1;
    private int copper = -1;

    public Gold(long money) {
        this.money = money;
    }

    protected Gold(Parcel in) {
        this.money = in.readLong();
        this.gold = in.readLong();
        this.silver = in.readInt();
        this.copper = in.readInt();
    }

    @Override
    public int hashCode() {
        return (int) (getMoney() ^ (getMoney() >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gold)) return false;

        Gold gold = (Gold) o;

        return getMoney() == gold.getMoney();

    }

    @Override
    public int compareTo(@NonNull Gold o) {
        return (int) (money - o.money);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.money);
        dest.writeLong(this.gold);
        dest.writeInt(this.silver);
        dest.writeInt(this.copper);
    }

    public String getCopper() {
        int copper = getCopperNum();
        return format.format(copper);
    }

    public String getGold() {
        long gold = getGoldNum();
        return goldFormat.format(gold);
    }

    public long getMoney() {
        return money;
    }

    public String getSilver() {
        int silver = getSilverNum();
        return format.format(silver);
    }

    public Spannable showSpan(Context context) {
        initSpan(context);
        SpannableStringBuilder builder = new SpannableStringBuilder(getGold()).append("金");
        int start = 0;
        int end = builder.length();
        builder.setSpan(goldColorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(goldSpan, end - 1, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        builder.append(getSilver()).append("银");
        start = end;
        end = builder.length();
        builder.setSpan(silverColorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(silverSpan, end - 1, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        builder.append(getCopper()).append("铜");
        start = end;
        end = builder.length();
        builder.setSpan(copperColorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(copperSpan, end - 1, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }

    private int getCopperNum() {
        if (copper < 0) {
            copper = (int) (money % 100);
        }
        return copper;
    }

    private long getGoldNum() {
        if (gold < 0) {
            gold = money / 10000;
        }
        return gold;
    }

    private int getSilverNum() {
        if (silver < 0) {
            silver = (int) (money % 10000 / 100);
        }
        return silver;
    }
}
