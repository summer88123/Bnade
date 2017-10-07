package com.summer.bnade.realm;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RealmFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class RealmFragment extends BaseFragment<RealmUIModel> {
    public static final String TAG = RealmFragment.class.getSimpleName();

    public RealmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Realm.
     */
    public static RealmFragment getInstance(FragmentManager fm) {
        RealmFragment fragment = (RealmFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RealmFragment();
        }
        return fragment;
    }

    @Override
    public int layout() {
        return R.layout.fragment_realm;
    }
}
