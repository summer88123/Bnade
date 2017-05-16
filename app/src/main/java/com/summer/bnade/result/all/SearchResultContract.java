package com.summer.bnade.result.all;

import com.summer.bnade.base.mvp.BaseView;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

/**
 * Created by kevin.bai on 2017/4/14.
 */

interface SearchResultContract {
    interface View extends BaseView<Presenter>{
        void show(SearchResultVO result);
    }

    interface Presenter {
        void filter(String query, Item item, Realm realm);

        void load(Item item, Realm realm);

    }
}
