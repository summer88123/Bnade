package com.summer.bnade.result;

import com.summer.bnade.base.BaseView;
import com.summer.bnade.search.entity.SearchResultVO;

/**
 * Created by kevin.bai on 2017/4/14.
 */

interface SearchResultContract {
    interface View extends BaseView<Presenter>{
        void show(SearchResultVO result);
    }

    interface Presenter {
        void filter(String query);

        void setData(SearchResultVO data);

        void load();

    }
}
