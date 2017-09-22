package com.here.util;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.Comment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/7/16 15:21
 */

public class CommentUtil {

    public interface OnQueryCommentListener{
        void success(List<Comment> comments);
        void fail(String error);
    }

    /**
     * 上传一个评论信息
     * @param comment
     * @param listener
     */
    public static void uploadComment(Comment comment
            , final UserUtil.OnDealListener listener){
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.success();
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
     * 查询帖子的对应的所有评论
     * @param publishId   发布帖子的Id
     * @param listener
     */
    public static void queryCommentOfPost(String publishId
            , final OnQueryCommentListener listener){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("publish",publishId);
        query.include("user");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null){
                    listener.success(list);
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



}
