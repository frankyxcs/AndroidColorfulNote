package com.product.colorfulnote.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.product.colorfulnote.R;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.activity.NavigationActivity;
import com.product.colorfulnote.ui.adapter.NoteListV2Adapter;
import com.product.colorfulnote.ui.base.AppBaseFragment;
import com.product.common.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import space.sye.z.library.RefreshRecyclerView;
import space.sye.z.library.adapter.RefreshRecyclerViewAdapter;
import space.sye.z.library.listener.OnBothRefreshListener;
import space.sye.z.library.manager.RecyclerMode;
import space.sye.z.library.manager.RecyclerViewManager;

/**
 * Created by Administrator on 2016-4-13.
 */
public class NoteListV2Fragment extends AppBaseFragment {
    private static final String TAG = NoteListV2Fragment.class.getSimpleName();
    private static final int INIT_COUNT = 5;
    private static final int PAGE_COUNT = 5;
    private static final int PULL_DOWN = 1;
    private static final int LOAD_MORE = 2;
    private static final long DELAY = 1000;

    private NoteListV2Adapter mAdapter;
    private ArrayList<Note> mNoteList;
    private int mCount = INIT_COUNT;

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;
//    @Bind(R.id.swipe_refresh)
//    SwipeRefreshLayout mSwipeRefresh;

    /**
     * 上拉下拉获取数据
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
            mRecyclerView.onRefreshCompleted();

            switch (msg.what) {
                case PULL_DOWN:
                    break;
                case LOAD_MORE:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG, "onCreate");
        EventBus.getDefault().register(this);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.content_navigation, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        RecyclerViewManager.with(mAdapter, new LinearLayoutManager(getActivity()))
                .setMode(RecyclerMode.BOTH)
                .setOnBothRefreshListener(new OnBothRefreshListener() {
                    @Override
                    public void onPullDown() {
                        refresh();
                    }

                    @Override
                    public void onLoadMore() {
                        loadMore();
                    }
                })
                .setOnItemClickListener(new RefreshRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                        ((NavigationActivity) getActivity()).gotoDetailFragment((Note) mNoteList.get(position));
                    }
                })
                .into(mRecyclerView, getActivity());
    }

    private void refresh() {
        mCount = INIT_COUNT;
        LogUtils.i(TAG, "refresh mCount = " + mCount);
        Log.i(TAG, "refresh mCount = " + mCount);
        noteGroupBy(mCount);
        Message msg = Message.obtain();
        msg.what = PULL_DOWN;
        mHandler.sendMessageDelayed(msg, DELAY);
    }

    private void loadMore() {
        mCount += PAGE_COUNT;
        LogUtils.i(TAG, "loadMore mCount = " + mCount);
        Log.i(TAG, "loadMore mCount = " + mCount);
        noteGroupBy(mCount);
        Message msg = Message.obtain();
        msg.what = LOAD_MORE;
        mHandler.sendMessageDelayed(msg, DELAY);
    }

    private void initData() {
        mNoteList = new ArrayList<>();
        noteGroupBy(mCount);
        mAdapter = new NoteListV2Adapter(getActivity(), mNoteList);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取指定长度的列表数据
     *
     * @param count
     */
    private void noteGroupBy(final int count) {
        mNoteList.clear();
        List<Note> noteList = DBNoteHelper.getInstance().loadAllByDate();
        if (noteList.isEmpty())
            return;

        for (int i = 0; i < count && i < noteList.size(); i++) {
            mNoteList.add(noteList.get(i));
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
            MobclickAgent.onEvent(getActivity(), "click");
            MobclickAgent.onEvent(getActivity(), "click", "ActionAdd");
            ((NavigationActivity) getActivity()).gotoDetailFragment(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(NoteDetailFragment event) {
        noteGroupBy(INIT_COUNT);
        mAdapter.notifyDataSetChanged();
        LogUtils.i(TAG, "onEventMainThread");
    }
}
