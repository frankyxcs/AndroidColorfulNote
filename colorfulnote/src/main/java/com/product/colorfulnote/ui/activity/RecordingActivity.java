package com.product.colorfulnote.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.product.colorfulnote.R;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.utils.CommonUtils;
import com.product.common.interfaces.IValid;
import com.product.common.utils.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
@Deprecated
public class RecordingActivity extends AppBaseActivity implements IValid {
    private static final String TAG = RecordingActivity.class.getSimpleName();
    private Note mNote;
    private String mTitle = "标题";
    private String mContent;

    //    @Bind(R.id.et_title)
//    EditText mEtTitle;
    @Bind(R.id.et_content)
    EditText mEtContent;
//    @Bind(R.id.btn_submit)
//    ShakeButton mBtnSubmit;

//    @OnClick(R.id.btn_submit)
//    void onClick(View view) {
//        complete();
//    }

    private void complete() {
        if (isValid()) {
            saveLocal();
            setResult(Activity.RESULT_OK, null);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        ButterKnife.bind(this);

        mNote = (Note) CommonUtils.getMaskSerializable(getIntent());
        // mEtTitle.setText(null != mNote ? mNote.getTitle() : null);
        mEtContent.setText(null != mNote ? mNote.getContent() : null);
    }

    @Override
    public boolean isValid() {
        // mTitle = mEtTitle.getText().toString().trim();
        mContent = mEtContent.getText().toString().trim();

        // CollapsingToolbarLayout
        if (StringUtils.isEmpty(mTitle)) {
            Snackbar.make(mEtContent, R.string.error_invalid_title, Snackbar.LENGTH_LONG).show();
//            mBtnSubmit.shake();
        } else if (StringUtils.isEmpty(mContent)) {
            Snackbar.make(mEtContent, R.string.error_invalid_content, Snackbar.LENGTH_LONG).show();
//            mBtnSubmit.shake();
        } else {
            return true;
        }
        return false;
    }

    private void saveLocal() {
        Note entiy = new Note(null, mTitle, mContent, new Date());
        if (null != mNote) {
            DBNoteHelper.getInstance().update(mNote, entiy);
        } else {
            DBNoteHelper.getInstance().save(entiy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_complete) {
            complete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
