package com.here.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.main.MainActivity;
import com.here.other.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;
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
    @Bind(R.id.iv_qq_login)
    ImageView ivQqLogin;
    @Bind(R.id.iv_wei_bo)
    ImageView ivWeiBo;

    private Dialog progressDialog;

    private SsoHandler mSsoHandler;

    private AuthInfo mAuthInfo;

    private boolean isHasWeibo = true ;


    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1106163416";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.translucentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTencent = Tencent.createInstance(APP_ID,LoginActivity.this.getApplicationContext());
        ButterKnife.bind(this);
        initView();
        mvpPresenter.attachView(this);
        try {
            mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
            mSsoHandler = new SsoHandler(this, mAuthInfo);
        } catch (Exception e) {
            e.printStackTrace();
            isHasWeibo = false;
        }
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

    @OnClick({R.id.tv_register, R.id.tv_login, R.id.et_account, R.id.iv_clean_account, R.id.et_password, R.id.iv_clean_password, R.id.iv_go,R.id.iv_qq_login, R.id.iv_wei_bo})
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
            case R.id.iv_qq_login:
                mIUiListener = new BaseUiListener();
                mTencent.login(LoginActivity.this,"all", mIUiListener);
                break;
            case R.id.iv_wei_bo:
                if (isHasWeibo){
                    mSsoHandler.authorize(new AuthListener());
                }
                break;
        }
    }

    @Override
    public boolean isLogin() {
        return ivLoginTips.getVisibility() == View.VISIBLE;
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
        if (progressDialog == null) {
            progressDialog = new Dialog(this, R.style.progress_dialog);
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
    public User getThridUser() {
        return thirdUser;
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




    private Oauth2AccessToken mAccessToken;


    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()) {
                getUserInfo();
            } else {
                String code = bundle.getString("code");//直接从bundle里边获取
                if (!TextUtils.isEmpty(code)) {
                    Toast.makeText(LoginActivity.this, "签名不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

            Toast.makeText(LoginActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

        private void getUserInfo() {
            long uid = Long.parseLong(mAccessToken.getUid());
            new UsersAPI(LoginActivity.this, Constants.APP_KEY, mAccessToken)
                    .show(uid, mListener);

        }

        //实现异步请求接口回调
        private RequestListener mListener = new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    com.sina.weibo.sdk.openapi.models.User user = com.sina.weibo.sdk.openapi.models.User.parse(response);
                    thirdUser = new User();
                    thirdUser.setUsername(user.id);
                    thirdUser.setHeadImageUrl(user.avatar_hd);
                    thirdUser.setBackgroundUrl(user.profile_image_url);
                    thirdUser.setSex(user.gender);
                    thirdUser.setAddress(user.province+"-"+user.city);
                    thirdUser.setPassword("123456");
                    thirdUser.setNickname(user.screen_name);
                    mvpPresenter.thirdUserLogin();

                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
            }
        };
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
        //SSO 授权回调
        //重要：发起sso登录的activity必须重写onActivtyResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    private User thirdUser;

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                thirdUser = new User();
                thirdUser.setUsername(openID);
                thirdUser.setPassword("123456");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        try {
                            Toast.makeText(LoginActivity.this, ((JSONObject) response).getString("nickname"), Toast.LENGTH_SHORT).show();
                            thirdUser.setNickname(((JSONObject) response).getString("nickname"));
                            thirdUser.setSex(((JSONObject) response).getString("gender"));
                            thirdUser.setAddress(((JSONObject) response).getString("province")+"-"+((JSONObject) response).getString("city"));
                            thirdUser.setHeadImageUrl(((JSONObject) response).getString("figureurl_qq_2"));
                            mvpPresenter.thirdUserLogin();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }
                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }
}

