package com.summer.bnade.utils;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by kevin.bai on 2017/4/17.
 */

public class DefaultViewUtil {

    public static void defaultRefresh(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);

        // 设定下拉圆圈的背景
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
    }
}
