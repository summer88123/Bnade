package com.summer.bnade.token;

import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BaseView;
import com.summer.bnade.token.entity.WowTokenVO;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public interface WowTokenContract {
    interface View extends BaseView<Presenter> {
        void showCurrentToken(WowTokenVO current);

        void showOneDayChart(List<Entry> data);

        void refreshOver();

        void refreshStart();
    }

    interface Presenter{
        void load();
    }
}
