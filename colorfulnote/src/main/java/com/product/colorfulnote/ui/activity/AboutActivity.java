package com.product.colorfulnote.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import com.product.colorfulnote.R;
import com.product.colorfulnote.ui.base.AppBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class AboutActivity extends AppBaseActivity {

    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        // 设置还没收缩时状态下字体颜色
        // mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        // 设置收缩后Toolbar上字体的颜色
        // mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);
    }
}
