package com.here.util;

import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.Follow;
import com.here.bean.FollowId;
import com.here.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hyc on 2017/7/15 19:33
 */

public class FollowUtil {

    public interface OnFindFollowListener{
        void success(List<Follow> follows);
        void fail(String error);
    }

    /**
     * 关注用户
     * @param user
     * @param followUser
     * @param listener
     */
    public static void followUser(final User user , final User followUser
            , final UserUtil.OnDealListener listener){
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
            if (followId.getUserId().equals(userId) && followId
                    .getFollowUserId().equals(followUserId)){
                return true;
            }
        }
        return false;
    }


    public static void cancelFollow(String userId , String followUserId
            , final UserUtil.OnDealListener listener){
        for (final FollowId followId : DataSupport.findAll(FollowId.class)) {
            if (followId.getUserId().equals(userId) && followId
                    .getFollowUserId().equals(followUserId)){
                Follow follow = new Follow();
                follow.setObjectId(followId.getFollowId());
                follow.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e== null){
                            listener.success();
                            followId.delete();
                        }else if (e.getErrorCode() == 9016){
                            listener.fail("网络不给力");
                        }else {
                            listener.fail(e.getMessage());
                        }
                    }
                });
                break;
            }
        }
    }

    /**
     * 查询关注
     * @param user
     * @param listener
     */
    public static void queryFollows(User user, final OnFindFollowListener listener){
        BmobQuery<Follow> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("followUser");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e == null){
                    listener.success(list);
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

    public static void queryFans(User user,final OnFindFollowListener listener){
        BmobQuery<Follow> query = new BmobQuery<>();
        query.addWhereEqualTo("followUser",user);
        query.include("user");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e == null){
                    listener.success(list);
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

    public static List<String> queryAllFollowId(String userId){
        List<FollowId> followIds = DataSupport.findAll(FollowId.class);
        List<String> strings = new ArrayList<>();
        for (FollowId followId : followIds) {
            if (userId.equals(followId.getUserId())){
                strings.add(followId.getFollowUserId());
            }
        }
        return strings;
    }


}
