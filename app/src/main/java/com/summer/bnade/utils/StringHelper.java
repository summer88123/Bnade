package com.summer.bnade.utils;

import android.content.Context;

import com.summer.bnade.R;
import com.summer.lib.model.entity.Gold;

/**
 * Created by kevin.bai on 2017/5/15.
 */

public class StringHelper {
    public static String fullGold(Context context, Gold gold) {
        return context.getString(R.string.full_gold, gold.getGold(), gold.getSilver(), gold.getCopper());
    }
}
