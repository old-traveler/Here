package com.here.password;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.here.R;
import com.here.base.MvpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordActivity extends MvpActivity<PasswordPresenter> implements PasswordContract {

    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.btn_update_password)
    Button btnUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_password);
        initHome();
    }

    @Override
    protected PasswordPresenter createPresenter() {
        return new PasswordPresenter();
    }


    @OnClick(R.id.btn_update_password)
    public void onViewClicked() {
        mvpPresenter.updatePassword();
    }

    @Override
    public String getOldPassword() {
        return etOldPassword.getText().toString();
    }

    @Override
    public String getNewPassword() {
        return etNewPassword.getText().toString();
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
        toastShow(R.string.update_password_success);
        etNewPassword.setText("");
        etOldPassword.setText("");
    }

    @Override
    public void updateFail(String error) {
        toastShow(error);
    }
}
