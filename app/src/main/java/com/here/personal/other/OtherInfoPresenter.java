package com.here.personal.other;

import com.here.base.BasePresenter;
import com.here.bean.Blacklist;
import com.here.bean.User;
import com.here.util.FollowUtil;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/7/18 10:58
 */

public class OtherInfoPresenter extends BasePresenter<OtherInfoContract> {

    public void load(){
        mvpView.setUserInfo(mvpView.getUserInfo());
    }



    public void joinBlackList(){
        Blacklist blacklist = new Blacklist();
        blacklist.setUser(BmobUser.getCurrentUser(User.class));
        blacklist.setBlacklistUser(mvpView.getUserInfo());
        mvpView.showLoading();
        blacklist.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null && mvpView != null){
                    mvpView.stopLoading();
                    mvpView.blacklistSuccess();
                } else if (mvpView != null) {
                    mvpView.stopLoading();
                    if (e.getErrorCode() == 9016){
                        mvpView.fail("网络不给力");
                    }else {
                        mvpView.fail(e.getMessage());
                    }
                }
            }
        });
    }

    public void followUser(){

        User my = BmobUser.getCurrentUser(User.class);
        User other = mvpView.getUserInfo();
        if(FollowUtil.isFollow(my.getObjectId(),other
                .getObjectId())){
            mvpView.fail("已关注该用户");
            return;
        }
        mvpView.showLoading();
        FollowUtil.followUser(my,other, new UserUtil
                .OnDealListener() {
            @Override
            public void success() {
                if (mvpView!=null){
                    mvpView.stopLoading();
                    mvpView.followSuccess();
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView!=null){
                    mvpView.stopLoading();
                    mvpView.fail(error);
                }
            }
        });
    }

}
