package com.summer.bnade.result.single;

import com.summer.bnade.base.BaseView;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

/**
 * Created by kevin.bai on 2017/4/14.
 */

interface ItemResultContract {
    interface View extends BaseView<Presenter>{
        void show(SearchResultVO result);
    }

    interface Presenter {
        void load(Item item, Realm realm);
    }
}
