package com.product.colorfulnote.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.product.colorfulnote.R;
import com.product.colorfulnote.common.Constants;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.utils.CommonUtils;
import com.product.common.utils.LogUtils;
import com.product.common.utils.TimeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
@Deprecated
public class RecordDetailActivity extends AppBaseActivity {
    private static final String TAG = RecordDetailActivity.class.getSimpleName();
    private Note mNote;

    @Bind(R.id.txt_record_title)
    TextView mTxtRecordTitle;
    @Bind(R.id.txt_record_date)
    TextView mTxtRecordDate;
    @Bind(R.id.txt_record_content)
    TextView mTxtRecordContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);

        initData();
        initTitle();
        initView();
    }

    private void initView() {
        if (null != mNote) {
            mTxtRecordTitle.setText(mNote.getTitle());
            mTxtRecordDate.setText(TimeUtils.getTime(mNote.getDate().getTime(), TimeUtils.DEFAULT_DATE_FORMAT));
            mTxtRecordContent.setText(mNote.getContent());
        }
    }

    private void initData() {
        mNote = (Note) CommonUtils.getMaskSerializable(getIntent());
    }

    private void initTitle() {
        if (null != mNote) {
            setTitle(TimeUtils.getTime(mNote.getDate().getTime(), TimeUtils.DATE_FORMAT_DAY));
        } else {
            setTitle(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DAY));
        }
    }

    private void refresh() {
        LogUtils.i(TAG, "refresh");
        mNote = DBNoteHelper.getInstance().load(DBNoteHelper.getInstance().getKey(mNote));

        initTitle();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult requestCode = " + requestCode + " ;resultCode = " + resultCode);
        if (Activity.RESULT_OK == resultCode) {
            if (Constants.COMMON_REQUEST_CODE == requestCode) {
                refresh();
                setResult(Activity.RESULT_OK);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_modify) {
            openActivityForResult(RecordingActivity.class,
                    Constants.COMMON_REQUEST_CODE, CommonUtils.getMaskBundle(mNote));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
