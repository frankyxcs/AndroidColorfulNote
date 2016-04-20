package com.product.colorfulnote.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.product.colorfulnote.BaseApplication;
import com.product.colorfulnote.R;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.thridpart.push.PushProxy;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.ui.fragment.NoteDetailFragment;
import com.product.colorfulnote.ui.fragment.NoteListV2Fragment;
import com.product.colorfulnote.ui.helper.ThemeHelper;
import com.product.colorfulnote.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String LIST_FRAGMENT = "ListFragment";
    public static final String DETAIL_FRAGMENT = "DetailFragment";

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        /** 初始化文件夹等环境 */
        CommonUtils.initAppEnvironment();
        /** 应用升级 */
        // UpdateProxy.getInstance().update(this);
        /** 百度push */
        PushProxy.getInstance().startWork(this);

        initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView(Bundle savedInstanceState) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.getHeaderView(0).findViewById(R.id.ly_portrait)
                .setBackgroundColor(getResources().getColor(ThemeHelper.getInstance().getItemBgColor()));
        // mNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, Fragment.instantiate(this, NoteListV2Fragment.class.getName(), null), LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void gotoDetailFragment(Note note) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)
                .replace(R.id.container, NoteDetailFragment.newInstance(note), DETAIL_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }


    private static final long EXIT_INTERVAL = 2000L;
    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if ((System.currentTimeMillis() - mExitTime) > EXIT_INTERVAL) {
                    showToast(R.string.common_exit_app);
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                    ((BaseApplication) getApplicationContext()).exitApp(true);
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
