package com.summer.bnade.realmrank;

import com.summer.bnade.base.BaseView;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public interface RealmRankContract {
    interface View extends BaseView<Presenter> {
        void show(List<AuctionRealm> list, AuctionRealm.SortType sortType);

        void setDependency(RealmRankAdapter adapter);

        void setRefreshing(boolean refreshing);
    }

    interface Presenter {
        void load();

        void sort(AuctionRealm.SortType sortType);
    }
}
