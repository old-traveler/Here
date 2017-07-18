package com.here.login;

import com.here.bean.User;

/**
 * Created by hyc on 2017/6/23 13:38
 */

public interface LoginContract {


    boolean isLogin();

    User getUserInfo();

    void showError(String msg);

    void showError(int msg);

    void showLoading();

    void loginSuccess();

    User getThridUser();

    void forgotPassword();

    void dismissLoading();

    boolean isFillInfo();

    String getPassword();

}
