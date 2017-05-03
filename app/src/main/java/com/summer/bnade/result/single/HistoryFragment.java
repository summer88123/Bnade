package com.summer.bnade.result.single;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.bnade.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends PageAdapter.PageFragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();

    public static HistoryFragment getInstance(FragmentManager fm) {
        HistoryFragment fragment = (HistoryFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new HistoryFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    int title() {
        return R.string.fragment_title_history_trand;
    }
}
