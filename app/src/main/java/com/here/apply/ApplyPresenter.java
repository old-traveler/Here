package com.here.apply;

import android.util.Log;

import com.here.base.BasePresenter;
import com.here.bean.Join;
import com.here.bean.User;
import com.here.util.ImActivityUtil;
import com.here.util.JoinUtil;
import com.here.util.UserUtil;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/11 20:04
 */

public class ApplyPresenter extends BasePresenter<ApplyContract> {

    private BmobIMUserInfo useId;

    public MessageEvent messageEvent;

    public void loadingData() {
        if (messageEvent == null){
            messageEvent = mvpView.getApplyUserInfo();
        }
        if (useId == null){
            mvpView.showLoading();
            UserUtil.searchUserInfoById(messageEvent.getMessage().getContent(), new UserUtil.OnSearchUserListener() {
                @Override
                public void success(User user) {
                    useId = new BmobIMUserInfo();
                    mvpView.stopLoading();
                    useId.setAvatar(user.getHeadImageUrl());
                    useId.setUserId(user.getObjectId());
                    useId.setName(user.getNickname());
                    mvpView.loadUserInfo(useId,user.getUsername());
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.fail(error);
                }
            });
        }



    }

    public void agree() {
        if (useId == null){
            mvpView.fail("加载用户信息出错");
            return;
        }
        mvpView.showLoading();
        ImActivityUtil.respondApply(useId, "agree-" + ImActivityUtil
                .getImActivityInfo(BmobUser.getCurrentUser(User.class))
                .getObjectId(), new UserUtil.OnDealListener() {
            @Override
            public void success() {
                uploadNewJoin();
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.fail(error);
            }
        });
    }

    public void uploadNewJoin() {
        User joinUser = new User();
        joinUser.setObjectId(useId.getUserId());
        Join join = new Join();
        join.setJoinUser(joinUser);
        join.setImActivity(ImActivityUtil.getImActivityInfo(BmobUser.getCurrentUser(User.class)));
        JoinUtil.uploadNewJoin(join, new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.stopLoading();
                mvpView.respondSuccess("同意消息已发出,请及时联系伙伴");
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.fail(error);
            }
        });
    }

    public void refuse() {
        if (useId == null){
            mvpView.fail("加载用户信息出错");
            return;
        }
        mvpView.showLoading();
        ImActivityUtil.respondApply(useId, "refuse-" + ImActivityUtil
                .getImActivityInfo(BmobUser.getCurrentUser(User.class))
                .getObjectId(), new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.stopLoading();
                mvpView.respondSuccess("已拒绝申请");
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.fail(error);
            }
        });
    }

}
