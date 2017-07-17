package com.here.util;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
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

    public static void queryMyPublish(User user , final OnQueryListener listener){
        BmobQuery<ImActivity> query = new BmobQuery<>();
        query.addWhereEqualTo("publisher",user);
        query.findObjects(new FindListener<ImActivity>() {
            @Override
            public void done(List<ImActivity> list, BmobException e) {
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

    public static void queryMyJoin(User user , final OnQueryListener listener){
        BmobQuery<Join> query = new BmobQuery<>();
        query.addWhereEqualTo("joinUser",user);
        query.include("imActivity");
        query.findObjects(new FindListener<Join>() {
            @Override
            public void done(List<Join> list, BmobException e) {
                if (e == null){
                    List<ImActivity> imActivities = new ArrayList<>();
                    for (Join join : list) {
                        imActivities.add(join.getImActivity());
                    }
                    listener.success(imActivities);
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
