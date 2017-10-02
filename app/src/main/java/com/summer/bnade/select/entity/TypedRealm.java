package com.summer.bnade.select.entity;

import com.summer.lib.model.entity.Realm;

/**
 * Created by kevin.bai on 2017/5/1.
 */

public class TypedRealm {
    public static final String LABEL_USED = "常用";
    private static final String LABEL_NORMAL = "全部";
    private String type;
    private Realm realm;

    private TypedRealm(String type, Realm realm) {
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

    public String getName(){
        return realm.getName();
    }

    public String getConnected(){
        return realm.getConnected();
    }

    public static TypedRealm USED(Realm realm){
        return new TypedRealm(LABEL_USED, realm);
    }

    public static TypedRealm NORMAL(Realm realm) {
        return new TypedRealm(LABEL_NORMAL, realm);
    }
}
