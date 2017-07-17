package com.here.follow.info;

import com.here.base.BasePresenter;
import com.here.bean.Follow;
import com.here.bean.User;
import com.here.util.FollowUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/16 23:38
 */

public class FollowPresenter extends BasePresenter<FollowContract> {

    private List<Follow> myFollows;

    private List<Follow> myFans;

    public void queryMyFollow(){
        if (myFollows == null){
            mvpView.showLoading();
            FollowUtil.queryFollows(BmobUser.getCurrentUser(User.class), new FollowUtil.OnFindFollowListener() {
                @Override
                public void success(List<Follow> follows) {
                    mvpView.stopLoading();
                    myFollows = follows;
                    mvpView.loadMyFollow(myFollows);
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.loadFail(error);
                }
            });
        }else {
            mvpView.loadMyFollow(myFollows);
        }
    }


    public void queryMyFans(){
        if (myFans == null){
            mvpView.showLoading();
            FollowUtil.queryFans(BmobUser.getCurrentUser(User.class), new FollowUtil.OnFindFollowListener() {
                @Override
                public void success(List<Follow> follows) {
                    myFans = follows;
                    mvpView.stopLoading();
                    mvpView.loadMyFans(myFans);
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.loadFail(error);
                }
            });
        }else {
            mvpView.loadMyFans(myFans);
        }
    }


}
