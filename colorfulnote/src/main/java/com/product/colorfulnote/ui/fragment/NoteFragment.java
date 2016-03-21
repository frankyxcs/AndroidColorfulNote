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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NoteFragment extends AppBaseFragment implements ExpandableListView.OnChildClickListener {
    private static final String TAG = NoteFragment.class.getSimpleName();
    private TimelineAdapter mAdapter;
    private List<Note> mNoteList;

    private int count = 0;

    @Bind(R.id.expandable_listview)
    ExpandableListView mExpListview;

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

    public NoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mNoteList = DBNoteHelper.getInstance().loadAllByDate();
        mAdapter = new TimelineAdapter(getAppBaseActivity(), mNoteList);
        mExpListview.setDivider(null);
        mExpListview.setGroupIndicator(null);
        mExpListview.setChildIndicator(null);
        mExpListview.setChildDivider(null);
        mExpListview.setAdapter(mAdapter);
        mExpListview.setOnChildClickListener(this);
        expandGroup();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void expandGroup() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mExpListview.expandGroup(i);
        }
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
                mNoteList = DBNoteHelper.getInstance().loadAllByDate();
                mAdapter.resetData(mNoteList);
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
