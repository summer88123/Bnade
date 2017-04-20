package com.summer.bnade.select;

import com.summer.bnade.base.BaseView;
import com.summer.lib.model.entity.Realm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/20.
 */

public interface RealmSelectContract {
    interface View extends BaseView<Presenter> {
        void show(List<Realm> list);
    }
    interface Presenter {
        void filter(CharSequence s);

        void load();
    }
}
