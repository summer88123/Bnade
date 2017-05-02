package com.summer.bnade.utils;

import com.summer.bnade.base.BaseView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/5/2.
 */

public class RxUtil {

    public static class BaseErrorHandler implements Consumer<Throwable> {
        BaseView mBaseView;

        public BaseErrorHandler(BaseView view) {
            this.mBaseView = view;
        }

        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
            mBaseView.showToast(throwable.getMessage());
        }
    }
}
