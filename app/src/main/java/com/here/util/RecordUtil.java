package com.here.util;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hyc on 2017/7/17 20:29
 */

public class RecordUtil  {
    public interface OnQueryListener{
        void success(List<ImActivity> imActivities);
        void fail(String error);
    }

    public interface OnQueryJoinListener{
        void success(List<Join> imActivities);
        void fail(String error);
    }


    public static void queryMyPublish(User user , final OnQueryListener listener){
        BmobQuery<ImActivity> query = new BmobQuery<>();
        query.addWhereEqualTo("publisher",user);
        query.order("-updatedAt");
        query.findObjects(new FindListener<ImActivity>() {
            @Override
            public void done(List<ImActivity> list, BmobException e) {
                if (e == null){
                    User user = BmobUser.getCurrentUser(User.class);
                    for (ImActivity imActivity : list) {
                        imActivity.setPublisher(user);
                    }
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

    public static void queryMyJoin(User user , final OnQueryJoinListener listener){
        BmobQuery<Join> query = new BmobQuery<>();
        query.addWhereEqualTo("joinUser",user);
        query.include("imActivity");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Join>() {
            @Override
            public void done(List<Join> list, BmobException e) {
                if (e == null){
                    for (Join join : list) {
                        join.setJoinUser(BmobUser.getCurrentUser(User.class));
                    }
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

    public static void queryUser(List<ImActivity> imActivity){
        BmobQuery<ImActivity> query = new BmobQuery<>();
        List<BmobQuery<ImActivity>> queries = new ArrayList<>();
        for (ImActivity activity : imActivity) {
            BmobQuery<ImActivity> querys = new BmobQuery<>();
            querys.addWhereEqualTo("objectId",activity.getObjectId());
            queries.add(querys);
        }
        query.or(queries);
        query.include("publisher");
        query.findObjects(new FindListener<ImActivity>() {
            @Override
            public void done(List<ImActivity> list, BmobException e) {
                if (e == null){

                }else {
                    if (e.getErrorCode() == 9016){

                    }else {

                    }
                }
            }
        });
    }


}
