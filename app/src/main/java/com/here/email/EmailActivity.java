package com.here.email;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.personal.PersonalPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class EmailActivity extends MvpActivity<EmailPresenter> implements EmailContract {

    @Bind(R.id.iv_email_back)
    ImageView ivEmailBack;
    @Bind(R.id.tv_email_complete)
    TextView tvEmailComplete;
    @Bind(R.id.et_email)
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        etEmail.setText(BmobUser.getCurrentUser(User.class).getEmail());
    }

    @Override
    protected EmailPresenter createPresenter() {
        return new EmailPresenter();
    }

    @OnClick({R.id.iv_email_back, R.id.tv_email_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_email_back:
                finish();
                break;
            case R.id.tv_email_complete:
                tvEmailComplete.setClickable(false);
                mvpPresenter.addUpdateEmail();
                break;
        }
    }

    @Override
    public String getEmail() {
        return etEmail.getText().toString();
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
    public void updateSuccess() {
        PersonalPresenter.isNeedRefreshUserData=true;
        toastShow(R.string.email_verify);
        finish();
    }

    @Override
    public void updateFail(String error) {
        toastShow(error);
        tvEmailComplete.setClickable(true);
        log(error);
    }
}
