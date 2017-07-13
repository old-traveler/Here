package com.here.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract {

    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.iv_clean_account)
    ImageView ivCleanAccount;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.iv_clean_password)
    ImageView ivCleanPassword;
    @Bind(R.id.iv_go)
    ImageView ivGo;
    @Bind(R.id.iv_register_tips)
    ImageView ivRegisterTips;
    @Bind(R.id.iv_login_tips)
    ImageView ivLoginTips;

    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.translucentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        mvpPresenter.attachView(this);
    }

    private void initView() {
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etAccount.getText().toString())) {
                    ivCleanAccount.setVisibility(View.GONE);
                } else {
                    ivCleanAccount.setVisibility(View.VISIBLE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    ivCleanPassword.setVisibility(View.GONE);
                    ivGo.setVisibility(View.GONE);
                } else {
                    ivCleanPassword.setVisibility(View.VISIBLE);
                    ivGo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.tv_register, R.id.tv_login, R.id.et_account, R.id.iv_clean_account, R.id.et_password, R.id.iv_clean_password, R.id.iv_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                ivRegisterTips.setVisibility(View.VISIBLE);
                ivLoginTips.setVisibility(View.GONE);
                break;
            case R.id.tv_login:
                ivRegisterTips.setVisibility(View.GONE);
                ivLoginTips.setVisibility(View.VISIBLE);
                break;
            case R.id.et_account:
                break;
            case R.id.iv_clean_account:
                etAccount.setText("");
                break;
            case R.id.et_password:
                break;
            case R.id.iv_clean_password:
                etPassword.setText("");
                break;
            case R.id.iv_go:
                mvpPresenter.loginOrRegister();
                break;
        }
    }

    @Override
    public boolean isLogin() {
        return ivLoginTips.getVisibility()==View.VISIBLE;
    }

    @Override
    public User getUserInfo() {
        User user = new User();
        user.setUsername(etAccount.getText().toString());
        user.setPassword(etPassword.getText().toString());
        return user;
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }

    @Override
    public void showError(int msg) {
        toastShow(msg);
    }

    @Override
    public void showLoading() {
        if (progressDialog==null){
            progressDialog = new Dialog(this,R.style.progress_dialog);
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("加载中");
            progressDialog.show();
        }
        progressDialog.show();

    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.zoom_enter,
                R.anim.zoom_exit);
        finish();
    }


    @Override
    public void forgotPassword() {

    }

    @Override
    public void dismissLoading() {
        progressDialog.dismiss();
    }

    @Override
    public boolean isFillInfo() {
        return !TextUtils.isEmpty(etAccount.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString());
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }
}
