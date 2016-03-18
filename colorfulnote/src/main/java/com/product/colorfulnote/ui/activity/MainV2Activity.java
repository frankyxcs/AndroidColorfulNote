package com.product.colorfulnote.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;

import com.product.colorfulnote.R;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.ui.fragment.NavigationDrawerV2Fragment;
import com.product.colorfulnote.ui.fragment.NoteFragment;

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
        mNavigationDrawerFragment.setUp(mDrawerLayout, mToolBar);
    }
}
