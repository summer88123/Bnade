package com.summer.bnade.base;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivityCreated{

    @Override
    public void injectComponent() {
        // nothing
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

}
