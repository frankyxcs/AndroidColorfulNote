package com.product.colorfulnote.ui.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.product.colorfulnote.R;
import com.product.colorfulnote.ui.helper.ThemeHelper;
import com.product.common.ui.base.BaseActivity;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public abstract class AppBaseActivity extends BaseActivity {
    protected Toolbar mToolBar;
    // protected TextView mToolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            mToolBar.setBackgroundResource(ThemeHelper.getInstance().getTitleBgColor());
            mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
//            mToolBarTitle = (TextView) findViewById(R.id.toolbar_title);
//            if (mToolBarTitle != null) {
//                getSupportActionBar().setDisplayShowTitleEnabled(false);
//            }
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
