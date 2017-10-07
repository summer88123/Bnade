package com.summer.bnade.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseActivity;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.search.SearchFragment;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Lazy;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    BottomNavigationView navigationView;
    @Inject
    FragmentManager fm;
    @Inject
    @Search
    Lazy<BaseFragment> search;
    @Inject
    @Realm
    Lazy<BaseFragment> realm;
    @Inject
    @PlayerItem
    Lazy<BaseFragment> playerItem;
    @Inject
    @Personal
    Lazy<BaseFragment> personal;

    BaseFragment current;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
    }

    @Override
    public int layout() {
        return R.layout.activity_main;
    }

    @Override
    public void setUpView() {
        setSupportActionBar(toolbar);
        fab.setOnClickListener(view -> {
            if (search.get() == current) {
                ((SearchFragment) search.get()).search();
            } else {
                selectSearch();
            }
        });

        navigationView.setOnNavigationItemSelectedListener(this);

        selectSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        BaseFragment next;
        if (id == R.id.btn_item_search) {
            next = search.get();
//        } else if (id == R.id.btn_token) {
//            next = token.get();
        } else if (id == R.id.btn_realm_rank) {
            next = realm.get();
        } else if (id == R.id.btn_player_item) {
            next = playerItem.get();
        } else if (id == R.id.btn_personal) {
            next = personal.get();
        } else {
            next = search.get();
        }
        current = next;
        fm.beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit)
                .replace(R.id.content_main, next, SearchFragment.TAG).commit();
        setTitle(item.getTitle());
        return true;
    }

    private void selectSearch() {
        navigationView.setSelectedItemId(R.id.btn_item_search);
    }

}
