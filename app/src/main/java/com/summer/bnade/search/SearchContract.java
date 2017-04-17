package com.summer.bnade.search;

import com.summer.bnade.base.BaseView;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.lib.model.entity.Hot;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/12.
 */

public interface SearchContract {
    interface View extends BaseView<Presenter> {
        void hideFuzzyList(String text);

        void show(SearchVO searchVO);

        void showFuzzySearch(SearchResultVO searchResultVO);

        void showResult(SearchResultVO searchResultVO);

        void updateHistories(List<String> histories);

        void updateHotSearch(List<Hot> hotList);
    }

    interface Presenter {
        void fuzzySearch(String text);

        void load();

        void search(String name, boolean saveHistory);

        void updateHotSearchType(int type);

        void clearHistories();
    }
}
