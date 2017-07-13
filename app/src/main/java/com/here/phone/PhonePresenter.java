package com.here.phone;


import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/6/24 11:12
 */

public class PhonePresenter extends BasePresenter<PhoneContract> {



    private String number;

    private boolean isCodeError=false;

    public void newPhoneNumber(){
        if (isCodeError){
            mvpView.verifyNumber();
        }else {
            mvpView.changePhoneNumber();
        }
    }

    public void verifyNumber(String number){
        mvpView.showLoading();
        this.number=number;
        UserUtil.verifyPhoneNumber(number, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.stopLoading();
                mvpView.verifyNumber();
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.verifyFail(error);
            }
        });
    }


    public void verifyCode(String code){
        mvpView.showLoading();
        UserUtil.verifyCode(number, code, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                updatePhoneNumber();
                isCodeError=false;
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.verifyFail(error);
                isCodeError=true;
            }
        });
    }

    public void updatePhoneNumber(){
            User user =BmobUser.getCurrentUser(User.class);
            user.setMobilePhoneNumber(number);
            user.setMobilePhoneNumberVerified(true);
            UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
                @Override
                public void success() {
                    mvpView.stopLoading();
                    mvpView.updateSuccess();
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.verifyFail(error);
                }
            });
    }



}
