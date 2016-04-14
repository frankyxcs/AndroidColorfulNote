package com.product.colorfulnote.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.product.colorfulnote.BaseApplication;
import com.product.colorfulnote.R;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.thridpart.push.PushProxy;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.ui.fragment.NoteDetailFragment;
import com.product.colorfulnote.ui.fragment.NoteListV2Fragment;
import com.product.colorfulnote.utils.CommonUtils;

public class NavigationActivity extends AppBaseActivity {
    public static final String LIST_FRAGMENT = "ListFragment";
    public static final String DETAIL_FRAGMENT = "DetailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        /** 初始化文件夹等环境 */
        CommonUtils.initAppEnvironment();
        /** 应用升级 */
        // UpdateProxy.getInstance().update(this);
        /** 百度push */
        PushProxy.getInstance().startWork(this);

        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


//    private static final long EXIT_INTERVAL = 2000L;
//    private long mExitTime = 0;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - mExitTime) > EXIT_INTERVAL) {
//                showToast(R.string.common_exit_app);
//                mExitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                ((BaseApplication) getApplicationContext()).exitApp(true);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
