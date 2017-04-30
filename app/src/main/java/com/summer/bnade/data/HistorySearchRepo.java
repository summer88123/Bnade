package com.summer.bnade.data;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class HistorySearchRepo {
    private static final String HISTORY_KEY = "KEY_HISTORY_SEARCH";
    private Set<String> cache;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    HistorySearchRepo(SharedPreferences sp) {
        this.mSp = sp;
        this.mEditor = mSp.edit();
        this.cache = mSp.getStringSet(HISTORY_KEY, new LinkedHashSet<String>());
    }

    public void add(String history){
        cache.add(history);
        save();
    }

    private void save() {
        mEditor.putStringSet(HISTORY_KEY, cache).apply();
    }

    public void clear() {
        cache.clear();
        save();
    }

    public List<String> getHistories() {
        return new ArrayList<>(cache);
    }
}
