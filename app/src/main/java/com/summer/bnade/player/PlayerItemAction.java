package com.summer.bnade.player;

import com.summer.lib.model.entity.Realm;

/**
 * Created by kevin.bai on 2017/6/13.
 */

public class PlayerItemAction {
    CharSequence query;
    Realm select;

    public PlayerItemAction(CharSequence query, Realm select) {
        this.query = query;
        this.select = select;
    }

    public CharSequence getQuery() {
        return query;
    }

    public Realm getSelect() {
        return select;
    }
}
