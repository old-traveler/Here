package com.here.util;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.Follow;
import com.here.bean.FollowId;
import com.here.bean.User;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/7/15 19:33
 */

public class FollowUtil {

    /**
     * 关注用户
     * @param user
     * @param followUser
     * @param listener
     */
    public static void followUser(final User user , final User followUser, final UserUtil.OnDealListener listener){
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollowUser(followUser);
        follow.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    FollowId followId = new FollowId();
                    followId.setFollowUserId(followUser.getObjectId());
                    followId.setFollowId(s);
                    followId.setUserId(user.getObjectId());
                    followId.save();
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 判断是否已经关注
     * @param userId
     * @param followUserId
     * @return
     */
    public static boolean isFollow(String userId , String followUserId){
        for (FollowId followId : DataSupport.findAll(FollowId.class)) {
            if (followId.getUserId().equals(userId) && followId.getFollowUserId().equals(followUserId)){
                return true;
            }
        }
        return false;
    }


}
