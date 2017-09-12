package com.here.login;

import android.util.Log;

import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.AccountUtil;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/6/23 13:38
 */

public class LoginPresenter extends BasePresenter<LoginContract>  {

    public void loginOrRegister(){
        if (mvpView.isFillInfo()){
            mvpView.showLoading();
            if (mvpView.isLogin()){
             login();
            }else {
                UserUtil.register(mvpView.getUserInfo(), new UserUtil.OnDealListener() {
                    @Override
                    public void success() {
                        login();
                    }

                    @Override
                    public void fail(String error) {
                        mvpView.dismissLoading();
                        mvpView.showError(error);
                    }
                });
            }
        }else {
            mvpView.showError(R.string.err_login_empty);
        }
    }

    public void login(){
        UserUtil.login(mvpView.getUserInfo(), new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.dismissLoading();
                mvpView.loginSuccess();
                User user = BmobUser.getCurrentUser(User.class);
                AccountUtil.addAccount(user.getName(),user.getUsername(),
                        mvpView.getPassword(),user.getHeadImageUrl(),false);
            }

            @Override
            public void fail(String error) {
                mvpView.dismissLoading();
                mvpView.showError(error);
            }
        });
    }

    public void thirdUserLogin(){
        mvpView.showLoading();
        mvpView.getThridUser().signUp(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null || e.getErrorCode() == 202){
                    e=null;
                    thirdLogin();
                }else{
                    mvpView.dismissLoading();
                    mvpView.showError(e.getMessage());
                }
            }
        });
    }

    public void thirdLogin(){
        UserUtil.login(mvpView.getThridUser(), new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.dismissLoading();
                mvpView.loginSuccess();
                User user = BmobUser.getCurrentUser(User.class);
                AccountUtil.addAccount(user.getName(),user.getUsername(),
                        mvpView.getPassword(),user.getHeadImageUrl(),false);
            }

            @Override
            public void fail(String error) {
                mvpView.dismissLoading();
                mvpView.showError(error);
            }
        });
    }
}
