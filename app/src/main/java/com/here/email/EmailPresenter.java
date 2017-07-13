package com.here.email;

import android.text.TextUtils;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/1 12:24
 */

public class EmailPresenter extends BasePresenter<EmailContract> {


    public void addUpdateEmail(){
        if (!TextUtils.isEmpty(mvpView.getEmail())){
            if (mvpView.getEmail().equals(BmobUser.getCurrentUser(User.class).getEmail())){
                if (!BmobUser.getCurrentUser(User.class).getEmailVerified()) {
                    mvpView.showLoading();
                    updateEmail();
                }
                return;
            }
            mvpView.showLoading();
            User user = BmobUser.getCurrentUser(User.class);
            user.setEmail(mvpView.getEmail());
            UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
                @Override
                public void success() {
                    updateEmail();
                }

                @Override
                public void fail(String error) {
                    mvpView.updateFail(error);
                    mvpView.stopLoading();
                }
            });


        }else {
            mvpView.updateFail(HereApplication.getContext().getString(R.string.email_empty));
        }
    }

    public void updateEmail(){

        User user = BmobUser.getCurrentUser(User.class);
        UserUtil.bindEmail(user, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.updateSuccess();
                mvpView.stopLoading();
            }
            @Override
            public void fail(String error) {
                mvpView.updateFail(error);
                mvpView.stopLoading();
            }
        });


    }


}
