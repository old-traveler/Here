package com.here.follow.info;

import android.util.Log;

import com.here.base.BasePresenter;
import com.here.bean.Follow;
import com.here.bean.User;
import com.here.util.DbUtil;
import com.here.util.FollowUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/16 23:38
 */

public class FollowPresenter extends BasePresenter<FollowContract> {

    public boolean isLoadFollow = false;

    public boolean isLoadFans = false;

    public void queryMyFollow(boolean isRefresh){
        if (!isRefresh){
            List<User> users = DbUtil.getInstance()
                    .queryCurrentUserFollowOrFans(true);
            if (users.size() > 0){
                mvpView.loadMyFollow(users);
            }else if (!isLoadFollow){
                loadMyFollow();
                isLoadFollow = true;
            }else if (users.size() == 0){
                mvpView.loadMyFollow(new ArrayList<User>());
            }
        }else {
            loadMyFollow();
        }
    }

    public void loadMyFollow(){
        mvpView.showLoading();
        FollowUtil.queryFollows(BmobUser.getCurrentUser(User.class),
                new FollowUtil.OnFindFollowListener() {
                    @Override
                    public void success(List<Follow> follows) {
                        mvpView.stopLoading();
                        for (Follow follow : follows) {
                            DbUtil.getInstance().addFollow(follow.getFollowUser(),true);
                        }
                        mvpView.loadMyFollow(DbUtil.getInstance()
                                .queryCurrentUserFollowOrFans(true));
                    }

                    @Override
                    public void fail(String error) {
                        mvpView.stopLoading();
                        mvpView.loadFail(error);
                        List<User> users = DbUtil.getInstance()
                                .queryCurrentUserFollowOrFans(true);
                        if (users.size() > 0){
                            mvpView.loadMyFollow(users);
                        }
                    }
        });
    }


    public void queryMyFans(boolean isRefresh){
        if (!isRefresh){
            List<User> users = DbUtil.getInstance()
                    .queryCurrentUserFollowOrFans(false);
            if (users.size() > 0){
                mvpView.loadMyFans(users);
            }else if (!isLoadFans){
                loadMyFans();
                isLoadFans = true;
            }else if (users.size() == 0){
                mvpView.loadMyFans(new ArrayList<User>());
            }
        }else {
            loadMyFans();
        }
    }

    public void loadMyFans() {
        mvpView.showLoading();
        FollowUtil.queryFans(BmobUser.getCurrentUser(User.class),
                new FollowUtil.OnFindFollowListener() {
                    @Override
                    public void success(List<Follow> follows) {
                        mvpView.stopLoading();
                        for (Follow follow : follows) {
                            DbUtil.getInstance().addFollow(follow.getUser(), false);
                        }
                        mvpView.loadMyFans(DbUtil.getInstance()
                                .queryCurrentUserFollowOrFans(false));
                    }

                    @Override
                    public void fail(String error) {
                        mvpView.stopLoading();
                        mvpView.loadFail(error);
                        List<User> users = DbUtil.getInstance()
                                .queryCurrentUserFollowOrFans(false);
                        if (users.size() > 0) {
                            mvpView.loadMyFans(users);
                        }
                    }
        });
    }

}
