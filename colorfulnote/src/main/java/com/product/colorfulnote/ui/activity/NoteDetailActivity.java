package com.product.colorfulnote.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.product.colorfulnote.R;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.base.AppBaseActivity;
import com.product.colorfulnote.ui.helper.ThemeHelper;
import com.product.colorfulnote.utils.CommonUtils;
import com.product.common.interfaces.IValid;
import com.product.common.utils.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
public class NoteDetailActivity extends AppBaseActivity implements IValid {
    private static final String TAG = NoteDetailActivity.class.getSimpleName();

    private Note mNote;
    private String mTitle = "标题";
    private String mContent;
    private MaterialDialog mMaterialDialog;

    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.ly_box)
    LinearLayout mLyBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mNote = (Note) CommonUtils.getMaskSerializable(getIntent());
    }

    private void initView() {
        mLyBox.setBackgroundResource(ThemeHelper.getInstance().getGroupBgColor());
        mEtContent.setText(isHasDetail() ? mNote.getContent() : null);
        mEtContent.setSelection(isHasDetail() ? mNote.getContent().length() : 0);
    }

    private boolean isHasDetail() {
        return (null != mNote) ? true : false;
    }

    private void complete() {
        if (isValid()) {
            showMaterialDialog();
        }
    }

    @Override
    public boolean isValid() {
        mContent = mEtContent.getText().toString().trim();

        if (StringUtils.isEmpty(mTitle)) {
            Snackbar.make(mEtContent, R.string.error_invalid_title, Snackbar.LENGTH_LONG).show();
        } else if (StringUtils.isEmpty(mContent)) {
            Snackbar.make(mEtContent, R.string.error_invalid_content, Snackbar.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

    private void saveLocal() {
        Note entiy = new Note(null, mTitle, mContent, new Date());
        if (isHasDetail()) {
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

        if (id == R.id.action_complete) {
            complete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!StringUtils.isEmpty(mEtContent.getText().toString().trim())) {
                if (null != mNote && mNote.getContent().equals(mEtContent.getText().toString().trim())) {
                    finish();
                } else {
                    complete();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog(this)
                //.setTitle("MaterialDialog")
                .setMessage(R.string.dlg_note_detail_content)
                .setPositiveButton(R.string.common_yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                        saveLocal();
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    }
                })
                .setNegativeButton(R.string.common_no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }
}
