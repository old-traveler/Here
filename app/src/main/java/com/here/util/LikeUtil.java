package com.here.util;

import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.Appointment;
import com.here.bean.Like;
import com.here.bean.LikeId;
import com.here.bean.User;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hyc on 2017/7/15 19:34
 */

public class LikeUtil {

    public interface OnLikeListener{
        void success(String id);
        void fail(String error);
    }

    public interface OnCountQuery{
        void success(int count);
        void fail(String error);
    }


    /**
     * 给活动或者心情帖点赞
     * @param user  点赞用户
     * @param objectId  帖子
     * @param listener  监听
     */
    public static void likePost(final User user
            , final String objectId, final OnLikeListener listener){
        final Like like = new Like();
        like.setUser(user);
        like.setPublish(objectId);
        like.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.success(s);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 删除活动或者心情帖
     * @param like      点赞
     * @param listener  监听
     */
    public static void deletePost(final Like like , final OnLikeListener listener){
        LikeId likeid = null;
        for (LikeId likeId : DataSupport.findAll(LikeId.class)) {
            if (likeId.getUserId().equals(like.getUser()
                    .getObjectId()) && likeId.getPublishId()
                    .equals(like.getPublish())){
                like.setObjectId(likeId.getLikeId());
                likeid = likeId;
                break;
            }
        }
        final LikeId finalLikeid = likeid;
        like.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    finalLikeid.delete();
                    listener.success(like.getObjectId());
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
     * 查询某个帖子的点赞数
     * @param objectId
     * @param listener
     */
    public static void queryLikeCount(String objectId , final OnCountQuery listener){
        BmobQuery<Like> query = new BmobQuery<>();
        query.addWhereEqualTo("publishId" , objectId);
        query.count(Like.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null){
                    listener.success(integer);
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

}
