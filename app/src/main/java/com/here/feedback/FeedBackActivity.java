package com.here.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.FeedBack;
import com.here.bean.User;
import com.here.login.LoginActivity;
import com.here.setting.SettingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class FeedBackActivity extends MvpActivity<FeedBackPresenter> implements FeedBackContract {


    @Bind(R.id.rb_advice)
    CheckBox rbAdvice;
    @Bind(R.id.rb_feedback_err)
    CheckBox rbFeedbackErr;
    @Bind(R.id.et_feedback)
    EditText etFeedback;
    @Bind(R.id.et_phone_number)
    EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_feedback);
        initHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit_feedback) {
            mvpPresenter.commitFeedback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected FeedBackPresenter createPresenter() {
        return new FeedBackPresenter();
    }


    @OnClick({R.id.rb_advice, R.id.rb_feedback_err})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_advice:
                rbAdvice.setChecked(true);
                if (rbFeedbackErr.isChecked()) {
                    rbFeedbackErr.setChecked(false);
                }
                break;
            case R.id.rb_feedback_err:
                rbFeedbackErr.setChecked(true);
                if (rbAdvice.isChecked()) {
                    rbAdvice.setChecked(false);
                }
                break;
        }
    }

    @Override
    public FeedBack getFeedback() {
        FeedBack feedBack = new FeedBack();
        feedBack.setError(rbFeedbackErr.isChecked());
        feedBack.setNumber(etPhoneNumber.getText().toString());
        feedBack.setFeedBack(etFeedback.getText().toString());
        feedBack.setSubmitter(BmobUser.getCurrentUser(User.class));
        return feedBack;
    }


    @Override
    public void showTips(String tips) {
        toastShow(tips);
    }

    @Override
    public boolean isSelectedType() {
        return rbAdvice.isChecked() || rbFeedbackErr.isChecked();
    }

    @Override
    public boolean isFillFeedback() {
        return !TextUtils.isEmpty(etFeedback.getText().toString());
    }


    @Override
    public boolean isFillNumber() {
        return !TextUtils.isEmpty(etPhoneNumber.getText().toString());
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void stopLoading() {
        dissmiss();
    }

    @Override
    public void feedBackSuccess() {
        new AlertView("提交成功", "感谢您的反馈", "确定", null, null, this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == -1) {
                    finish();
                }
            }
        }).show();
    }
}
