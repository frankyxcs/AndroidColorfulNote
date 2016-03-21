package com.product.colorfulnote.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.product.colorfulnote.R;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.ui.view.ShakeButton;
import com.product.colorfulnote.utils.CommonUtils;
import com.product.common.interfaces.IValid;
import com.product.common.utils.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
public class RecordingActivity extends AppBaseActivity implements IValid {
    private static final String TAG = RecordingActivity.class.getSimpleName();
    private Note mNote;
    private String mTitle;
    private String mContent;

    @Bind(R.id.et_title)
    EditText mEtTitle;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.btn_submit)
    ShakeButton mBtnSubmit;

    @OnClick(R.id.btn_submit)
    void onClick(View view) {
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
        mEtTitle.setText(null != mNote ? mNote.getTitle() : null);
        mEtContent.setText(null != mNote ? mNote.getContent() : null);
    }

    @Override
    public boolean isValid() {
        mTitle = mEtTitle.getText().toString().trim();
        mContent = mEtContent.getText().toString().trim();

        if (StringUtils.isEmpty(mTitle)) {
            showToast(R.string.error_invalid_title);
            mBtnSubmit.shake();
        } else if (StringUtils.isEmpty(mContent)) {
            showToast(R.string.error_invalid_content);
            mBtnSubmit.shake();
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
}
