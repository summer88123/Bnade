package com.summer.bnade.utils;

import android.util.Log;

import com.summer.bnade.base.mvp.BaseView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/5/2.
 */

public class RxUtil {
    private static final String TAG = RxUtil.class.getSimpleName();
    public static class BaseErrorHandler implements Consumer<Throwable> {
        BaseView mBaseView;

        public BaseErrorHandler(BaseView view) {
            this.mBaseView = view;
        }

        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
            Log.e(TAG, throwable.getMessage(), throwable);
            mBaseView.showToast(throwable.getMessage());
        }
    }
}
