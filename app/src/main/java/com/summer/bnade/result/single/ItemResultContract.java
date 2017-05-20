package com.summer.bnade.result.single;

import android.support.v4.util.Pair;

import com.summer.bnade.base.mvp.BaseView;
import com.summer.bnade.result.single.entity.AuctionHistoryVO;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import io.reactivex.SingleTransformer;

/**
 * Created by kevin.bai on 2017/4/14.
 */

interface ItemResultContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter {
        SingleTransformer<Pair<Item,Realm>,AuctionHistoryVO> history();

        SingleTransformer<Pair<Item, Realm>, List<AuctionRealmItem>> price();
    }
}
