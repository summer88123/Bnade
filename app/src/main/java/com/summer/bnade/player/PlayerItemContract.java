package com.summer.bnade.player;

import com.summer.bnade.base.BaseView;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.Realm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public interface PlayerItemContract {
    interface View extends BaseView<Presenter> {
        void showList(List<Auction> auctions);
    }

    interface Presenter {
        void search(String player, Realm realm);
    }
}
