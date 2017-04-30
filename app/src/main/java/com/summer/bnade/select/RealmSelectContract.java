package com.summer.bnade.select;

import com.summer.bnade.base.BaseView;
import com.summer.bnade.select.entity.RealmSelectVO;
import com.summer.lib.model.entity.Realm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/20.
 */

interface RealmSelectContract {
    interface View extends BaseView<Presenter> {
        void show(RealmSelectVO vo);

        void selected(Realm realm);

        void show(List<Realm> realms);
    }
    interface Presenter {
        void filter(CharSequence s);

        void load();

        void selectHistory(String realm);
    }
}
