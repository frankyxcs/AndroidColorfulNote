package com.product.colorfulnote.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.product.colorfulnote.R;
import com.product.colorfulnote.common.Constants;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.ui.fragment.NavigationDrawerV2Fragment;
import com.product.colorfulnote.ui.fragment.NoteFragment;
import com.product.common.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainV2Activity extends AppBaseActivity {
    private static final String TAG = MainV2Activity.class.getSimpleName();

    private NavigationDrawerV2Fragment mNavigationDrawerFragment;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.app_name);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, Fragment.instantiate(this, NoteFragment.class.getName(), null))
                .commit();

        mNavigationDrawerFragment = (NavigationDrawerV2Fragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // mNavigationDrawerFragment.setUp(mDrawerLayout, mToolBar);

        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult requestCode = " + requestCode + " ;resultCode = " + resultCode);
        if (Activity.RESULT_OK == resultCode) {
            if (Constants.COMMON_REQUEST_CODE == requestCode) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (null != fragment) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
