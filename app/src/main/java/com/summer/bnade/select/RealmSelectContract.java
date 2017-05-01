package com.summer.bnade.select;

import com.summer.bnade.base.BaseView;
import com.summer.bnade.select.entity.TypedRealm;
import com.summer.lib.model.entity.Realm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/20.
 */

interface RealmSelectContract {
    interface View extends BaseView<Presenter> {
        void selected(Realm realm);

        void show(List<TypedRealm> realms);
    }
    interface Presenter {
        void filter(CharSequence s);

        void load();
    }
}
