package com.summer.bnade.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseActivity;
import com.summer.bnade.base.di.ComponentHolder;
import com.summer.bnade.player.PlayerItemFragment;
import com.summer.bnade.player.PlayerItemModule;
import com.summer.bnade.realmrank.RealmRankFragment;
import com.summer.bnade.realmrank.RealmRankModule;
import com.summer.bnade.search.SearchFragment;
import com.summer.bnade.search.SearchModule;
import com.summer.bnade.token.WowTokenFragment;
import com.summer.bnade.token.WowTokenModule;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Provider<MainComponent> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    FragmentManager fm;

    MainComponent mMainComponent;

    @Override
    public int layout() {
        return R.layout.activity_main;
    }

    @Override
    public void setUpView() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = (SearchFragment) fm.findFragmentByTag(SearchFragment.TAG);
                if (searchFragment != null) {
                    searchFragment.search();
                } else {
                    selectSearch();
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        selectSearch();
    }

    @Override
    public MainComponent provide() {
        return mMainComponent;
    }

    private void selectSearch() {
        showSearch();
        navigationView.setCheckedItem(R.id.btn_item_search);
        setTitle(R.string.item_search);
    }

    @Override
    public void injectComponent() {
        fm = getSupportFragmentManager();
        mMainComponent = DaggerMainComponent.builder()
                .appComponent(ComponentHolder.getComponent())
                .wowTokenModule(new WowTokenModule(WowTokenFragment.getInstance(fm)))
                .searchModule(new SearchModule(SearchFragment.getInstance(fm)))
                .realmRankModule(new RealmRankModule(RealmRankFragment.getInstance(fm)))
                .playerItemModule(new PlayerItemModule(PlayerItemFragment.getInstance(fm)))
                .build();
        mMainComponent.inject(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.btn_item_search) {
            showSearch();
        } else if (id == R.id.btn_token) {
            WowTokenFragment token = (WowTokenFragment) mMainComponent.wowTokenView();
            fm.beginTransaction().replace(R.id.content_main, token, WowTokenFragment.TAG).commit();
        } else if (id == R.id.btn_realm_rank) {
            RealmRankFragment token = (RealmRankFragment) mMainComponent.realmRankView();
            fm.beginTransaction().replace(R.id.content_main, token, RealmRankFragment.TAG).commit();
        } else if (id == R.id.btn_player_item) {
            PlayerItemFragment player = (PlayerItemFragment) mMainComponent.playerItemView();
            fm.beginTransaction().replace(R.id.content_main, player, PlayerItemFragment.TAG).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        closeSoftInput();
        return true;
    }

    private void closeSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(drawer.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showSearch() {
        SearchFragment search = (SearchFragment) mMainComponent.searchView();
        fm.beginTransaction().replace(R.id.content_main, search, SearchFragment.TAG).commit();
    }

}
