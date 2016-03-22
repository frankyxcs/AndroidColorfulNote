package com.product.colorfulnote.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.product.colorfulnote.R;
import com.product.colorfulnote.common.Constants;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.activity.RecordDetailActivity;
import com.product.colorfulnote.ui.activity.RecordingActivity;
import com.product.colorfulnote.ui.adapter.TimelineAdapter;
import com.product.colorfulnote.ui.base.AppBaseFragment;
import com.product.colorfulnote.utils.CommonUtils;
import com.product.common.utils.LogUtils;
import com.product.common.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//import android.widget.ExpandableListView;

// import android.widget.ExpandableListView;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NoteFragment extends AppBaseFragment implements ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener {
    private static final String TAG = NoteFragment.class.getSimpleName();
    private static final int PAGE = 1;
    private static final int PAGE_COUNT = 1;
    private static final long DELAY = 100;

    private TimelineAdapter mAdapter;
    private int mPage = PAGE;

    @Bind(R.id.expandable_listview)
    PullToRefreshExpandableListView mExpListview;

//    @Bind(R.id.button_add)
//    Button buttonAdd;
//    @Bind(R.id.button_get)
//    Button buttonGet;
//
//    @OnClick(R.id.button_add)
//    void addData() {
//        count++;
//        Note entiy = new Note(null, "标题" + count, "内容" + count, new Date());
//        DBNoteHelper.getInstance().save(entiy);
//    }
//
//    @OnClick(R.id.button_get)
//    void getData() {
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public NoteFragment() {
    }

    private void initView() {
        // List<Note> noteList = DBNoteHelper.getInstance().loadAllByDate();
        mAdapter = new TimelineAdapter(getAppBaseActivity(), getGroupNotes(PAGE));
        mExpListview.getRefreshableView().setDivider(null);
        mExpListview.getRefreshableView().setGroupIndicator(null);
        mExpListview.getRefreshableView().setChildIndicator(null);
        mExpListview.getRefreshableView().setChildDivider(null);
        mExpListview.getRefreshableView().setAdapter(mAdapter);
        mExpListview.getRefreshableView().setOnGroupClickListener(this);
        mExpListview.getRefreshableView().setOnChildClickListener(this);

        mExpListview.setMode(PullToRefreshBase.Mode.BOTH);
        mExpListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                mExpListview.getLoadingLayoutProxy().setLastUpdatedLabel(
                        TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_MM));
                pullDown();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                mExpListview.getLoadingLayoutProxy().setLastUpdatedLabel(
                        TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_MM));
                pullUp();
            }
        });

        expandGroup();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private ArrayList<Note> getGroupNotes(final int groupPage) {
        List<Note> noteList = DBNoteHelper.getInstance().loadAllByDate();
        return groupBy(noteList, groupPage);
    }

    private ArrayList<Note> groupBy(final List<Note> noteList, final int groupPage) {
        ArrayList<Note> childData = new ArrayList<>();
        String preDate = null;
        int count = 0;

        for (Note entiy : noteList) {
            String date = TimeUtils.getTime(entiy.getDate().getTime(), TimeUtils.DATE_FORMAT_DAY);

            // 根据日期分组
            if (null == preDate || !preDate.equals(date)) {
                count++;
            }
            if (count > groupPage) {
                break;
            }

            childData.add(entiy);
            preDate = date;
        }
        return childData;
    }

    /**
     * 重新设置xlistview的状态
     */
    private void refreshComplete() {
        mExpListview.onRefreshComplete();
    }

    /**
     * 快速刷新回调
     */
    private void refreshCompleteQuick() {
        mExpListview.postDelayed(new Runnable() {
            @Override
            public void run() {
                mExpListview.onRefreshComplete();
            }
        }, DELAY);
    }

    private void pullDown() {
        mPage = PAGE;
        LogUtils.i(TAG, "pullDown mPage = " + mPage);
        mAdapter.resetData(getGroupNotes(mPage));
        expandGroup();
        refreshCompleteQuick();
    }

    private void pullUp() {
        mPage += PAGE_COUNT;
        LogUtils.i(TAG, "pullUp mPage = " + mPage);
        mAdapter.resetData(getGroupNotes(mPage));
        expandGroup();
        refreshCompleteQuick();
    }

    private void expandGroup() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mExpListview.getRefreshableView().expandGroup(i);
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        return true; // 列表都展开显示
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        Note entiy = (Note) mAdapter.getChild(groupPosition, childPosition);
        getAppBaseActivity().openActivityForResult(RecordDetailActivity.class,
                Constants.COMMON_REQUEST_CODE, CommonUtils.getMaskBundle(entiy));
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult requestCode = " + requestCode + " ;resultCode = " + resultCode);
        if (Activity.RESULT_OK == resultCode) {
            if (Constants.COMMON_REQUEST_CODE == requestCode) {
                mAdapter.resetData(getGroupNotes(mPage));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.

        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            getAppBaseActivity().openActivityForResult(
                    RecordingActivity.class, Constants.COMMON_REQUEST_CODE, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
