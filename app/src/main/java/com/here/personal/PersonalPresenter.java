package com.here.personal;

import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.main.MainPresenter;
import com.here.util.TinyUtil;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/6/25 19:57
 */

public class PersonalPresenter extends BasePresenter<PersonalContract> {

    public static boolean isNeedRefreshUserData=false;

    public void updateInfo(){
        if (isNeedRefreshUserData){
            isNeedRefreshUserData = false;
            mvpView.initUserData();
        }else {
        }
    }


    public  void updateAge(int age){
        User user = BmobUser.getCurrentUser(User.class);
        user.setAge(age);
        mvpView.showLoading();
        UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.initUserData();
                mvpView.stopLoading();
            }

            @Override
            public void fail(String error) {
                mvpView.updateFail(error);
                mvpView.stopLoading();
            }
        });

    }

    public void updateSex(String sex){
        User user = BmobUser.getCurrentUser(User.class);
        user.setSex(sex);
        mvpView.showLoading();
        UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.initUserData();
                mvpView.stopLoading();
            }

            @Override
            public void fail(String error) {
                mvpView.updateFail(error);
                mvpView.stopLoading();
            }
        });
    }

    public void updateAddress(String address){
        User user = BmobUser.getCurrentUser(User.class);
        user.setAddress(address);
        mvpView.showLoading();
        UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.initUserData();
                mvpView.stopLoading();
            }

            @Override
            public void fail(String error) {
                mvpView.updateFail(error);
                mvpView.stopLoading();
            }
        });
    }

    public void updateBirthday(String day){
        User user = BmobUser.getCurrentUser(User.class);
        user.setDateOfBirth(day);
        mvpView.showLoading();
        UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.initUserData();
                mvpView.stopLoading();
            }

            @Override
            public void fail(String error) {
                mvpView.updateFail(error);
                mvpView.stopLoading();
            }
        });
    }

    public void updateHead(String path){
        mvpView.showLoading();
        TinyUtil.compress(path, new TinyUtil.OnCompressListener() {
            @Override
            public void success(String out) {
                UserUtil.updateUserHeadImage(out, new UserUtil.OnImageDealListener() {
                    @Override
                    public void success(String path) {
                        MainPresenter.isUpdate = true;
                        mvpView.stopLoading();
                        mvpView.initUserData();
                    }

                    @Override
                    public void fail(String error) {
                        mvpView.updateFail(error);
                        mvpView.stopLoading();
                    }
                });
            }

            @Override
            public void fail() {
                mvpView.updateFail("上传失败，稍后再试");
            }
        });
    }


    public void updateBackground(String path){
        mvpView.showLoading();
        TinyUtil.compress(path, new TinyUtil.OnCompressListener() {
            @Override
            public void success(String out) {
                UserUtil.updateUserBacgound(out, new UserUtil.OnImageDealListener() {
                    @Override
                    public void success(String path) {
                        MainPresenter.isUpdate=true;
                        mvpView.stopLoading();
                        mvpView.initUserData();
                    }

                    @Override
                    public void fail(String error) {
                        mvpView.updateFail(error);
                        mvpView.stopLoading();
                    }
                });
            }

            @Override
            public void fail() {
                mvpView.updateFail("上传失败，稍后再试");
            }
        });
    }


}
