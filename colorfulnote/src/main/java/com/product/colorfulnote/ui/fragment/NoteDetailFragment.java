package com.product.colorfulnote.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.product.colorfulnote.R;
import com.product.colorfulnote.common.Constants;
import com.product.colorfulnote.db.DBNoteHelper;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.base.AppBaseFragment;
import com.product.colorfulnote.ui.helper.ThemeHelper;
import com.product.common.interfaces.IValid;
import com.product.common.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016-4-13.
 */
public class NoteDetailFragment extends AppBaseFragment implements IValid {
    private static final String TAG = NoteDetailFragment.class.getSimpleName();

    private Note mNote;
    private String mTitle = "标题";
    private String mContent;
    private MaterialDialog mMaterialDialog;

    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.ly_box)
    LinearLayout mLyBox;

    public NoteDetailFragment() {
    }

    public static NoteDetailFragment newInstance(Note note) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(Constants.SPKey.BUNDLE_KEY, note);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mNote = (Note) bundle.getSerializable(Constants.SPKey.BUNDLE_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
            showMaterialDialog(getActivity());
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (!StringUtils.isEmpty(mEtContent.getText().toString().trim())) {
//                MobclickAgent.onEvent(this, "keyboard");
//                MobclickAgent.onEvent(this, "keyboard", "back");
//                if (null != mNote && mNote.getContent().equals(mEtContent.getText().toString().trim())) {
//                    // finish();
//                } else {
//                    complete();
//                }
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    private void showMaterialDialog(final Context context) {
        mMaterialDialog = new MaterialDialog(context)
                //.setTitle("MaterialDialog")
                .setMessage(R.string.dlg_note_detail_content)
                .setPositiveButton(R.string.common_yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "click");
                        MobclickAgent.onEvent(context, "click", "MaterialDialog->Yes");

                        mMaterialDialog.dismiss();

                        saveLocal();
                        EventBus.getDefault().post(this);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(R.string.common_no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "click");
                        MobclickAgent.onEvent(context, "click", "MaterialDialog->No");

                        mMaterialDialog.dismiss();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
        mMaterialDialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        inflater.inflate(R.menu.menu_complete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_complete) {
            MobclickAgent.onEvent(getActivity(), "click");
            MobclickAgent.onEvent(getActivity(), "click", "ActionComplete");
            complete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
