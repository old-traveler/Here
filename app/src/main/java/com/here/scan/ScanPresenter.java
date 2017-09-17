package com.here.scan;

import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.FollowUtil;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/6/23 09:23
 */

public class ScanPresenter extends BasePresenter<ScanContract> {


    public void follow(String objectId){
        if (FollowUtil.isFollow(BmobUser.getCurrentUser(
                User.class).getObjectId(),objectId)){
            mvpView.followFail("已关注该用户");
            return;
        }
        mvpView.showLoading();
        User followUser = new User();
        followUser.setObjectId(objectId);
        FollowUtil.followUser(BmobUser.getCurrentUser(User
                .class), followUser, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.stopLoading();
                mvpView.followSuccess();
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.followFail(error);
            }
        });

    }


}
