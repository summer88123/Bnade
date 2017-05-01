package com.summer.bnade.select.entity;

import com.summer.lib.model.entity.Realm;

/**
 * Created by kevin.bai on 2017/5/1.
 */

public class TypedRealm {
    private static final String USED = "常用";
    private static final String NORMAL = "全部";
    private String type;
    private Realm realm;

    public TypedRealm(String type, Realm realm) {
        this.type  = type;
        this.realm = realm;
    }

    public String getType() {
        return type;
    }

    public Realm getRealm() {
        return realm;
    }

    public long getId(){
        return realm.getId();
    }

    public String getConnected(){
        return realm.getConnected();
    }

    public static TypedRealm USED(Realm realm){
        return new TypedRealm(USED, realm);
    }

    public static TypedRealm NORMAL(Realm realm) {
        return new TypedRealm(NORMAL, realm);
    }
}
