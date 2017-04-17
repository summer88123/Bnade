package com.summer.bnade.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kevin.bai on 2017/4/11.
 */

public class DateUtil {
    private DateUtil() {
        throw new IllegalStateException("不能实例化工具类");
    }

    private static ThreadLocal<SimpleDateFormat> map;

    static {
        map = new ThreadLocal<SimpleDateFormat>() {
            @Override
            public SimpleDateFormat get() {
                return new SimpleDateFormat();
            }
        };
    }

    public static String format(long time, String pattern) {
        SimpleDateFormat sdf = map.get();
        sdf.applyPattern(pattern);
        return sdf.format(new Date(time));
    }
}
