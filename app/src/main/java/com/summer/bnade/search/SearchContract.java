package com.summer.bnade.search;

import com.summer.bnade.base.mvp.BaseView;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/12.
 */

public interface SearchContract {
    interface View extends BaseView<Presenter> {
        void show(SearchVO searchVO);

        void search(String query);

        void showFuzzySearch(List<String> names);

        void showRealmItemResult(Item item, Realm realm);

        void showResult(Item item, Realm realm);

        void updateHistories(List<String> histories);

        void updateHotSearch(List<Hot> hotList);
    }

    interface Presenter {
        void load(int hotType);

        void search(String name, Realm realm);

        void updateHistory();

        void updateHotSearchType(int type);

        void clearHistories();
    }
}
