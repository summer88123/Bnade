package com.summer.bnade.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseFragment extends Fragment{
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
