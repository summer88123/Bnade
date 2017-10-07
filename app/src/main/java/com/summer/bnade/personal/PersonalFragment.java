package com.summer.bnade.personal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends BaseFragment<PersonalUIModel> {
    public static final String TAG = PersonalFragment.class.getSimpleName();

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment getInstance(FragmentManager fm) {
        PersonalFragment fragment = (PersonalFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PersonalFragment();
        }
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layout() {
        return R.layout.fragment_personal;
    }
}
