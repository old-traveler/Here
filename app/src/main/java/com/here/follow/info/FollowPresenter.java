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

    /**
     * 记录是否加载过云端关注记录
     */
    public boolean isLoadFollow = false;
    /**
     * 记录是否加载过云端粉丝信息
     */
    public boolean isLoadFans = false;

    /**
     * 查询当前用户关注的用户用户信息
     * 默认加载本地数据库中的信息
     * 弱请求刷新或数据为0时会请求云端信息
     * @param isRefresh true 刷新关注信息
     *  false加载本地数据库中的信息
     */
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

    /**
     * 加载当前用户云端的关注用户信息，并存入本地数据库
     */
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

    /**
     * 查询当前用户所有粉丝的用户信息
     * 默认加载本地数据库中的信息
     * 弱请求刷新或数据为0时会请求云端信息
     * @param isRefresh true 刷新关注信息
     *  false加载本地数据库中的信息
     */
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

    /**
     * 加载当前用户云端的粉丝用户信息，并存入本地数据库
     */
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
