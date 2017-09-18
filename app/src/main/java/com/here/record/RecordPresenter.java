package com.here.record;

import android.util.Log;
import android.widget.Toast;

import com.here.HereApplication;
import com.here.base.BasePresenter;
import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;
import com.here.util.DbUtil;
import com.here.util.RecordUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/17 18:49
 */

public class RecordPresenter extends BasePresenter<RecordContract> {

    public boolean isLoadMyPublish = false;

    public boolean isLoadMyJoin = false;


    public void queryMyPublish(boolean isRefresh){
        if (!isRefresh){
            List<ImActivity> imActivities = DbUtil.getInstance()
                    .queryMyPublisher(BmobUser.getCurrentUser(User.class));
            if (imActivities.size() > 0){
                mvpView.loadMyPublish(imActivities);
            }else if (!isLoadMyPublish){
                loadMyPublish();
                isLoadMyPublish = true;
            }else if (imActivities.size() == 0){
                mvpView.loadMyPublish(new ArrayList<ImActivity>());
            }
        }else {
            loadMyPublish();
        }
    }
    public void loadMyPublish(){
        mvpView.showLoading();
        RecordUtil.queryMyPublish(BmobUser.getCurrentUser(User.class), new RecordUtil.OnQueryListener() {
            @Override
            public void success(List<ImActivity> imActivities) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.loadMyPublish(imActivities);
                    for (ImActivity imActivity : imActivities) {
                        DbUtil.getInstance().addAppointment(imActivity);
                    }
                }
            }
            @Override
            public void fail(String error) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.loadFail(error);
                }
                List<ImActivity> imActivities = DbUtil.getInstance()
                        .queryMyPublisher(BmobUser.getCurrentUser(User.class));
                if (imActivities.size() > 0){
                    mvpView.loadMyPublish(imActivities);
                }
            }
        });
    }

    public void queryMyJoin(boolean isRefresh){
        if (!isRefresh){
            List<ImActivity> imActivities = DbUtil.getInstance()
                    .queryMyJoinImActivity(BmobUser.getCurrentUser(User.class));
            if (imActivities.size() > 0){
                mvpView.loadMyJoin(imActivities);
            }else if (!isLoadMyJoin){
                loadMyJoin();
                isLoadMyJoin = true;
            }else if (imActivities.size() == 0){
                mvpView.loadMyJoin(new ArrayList<ImActivity>());
            }
        }else {
            loadMyJoin();
        }
    }

    public void loadMyJoin(){
        mvpView.showLoading();
        RecordUtil.queryMyJoin(BmobUser.getCurrentUser(User.class), new RecordUtil.OnQueryJoinListener() {

            @Override
            public void success(List<Join> joins) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    for (Join join : joins) {
                        Log.i("TAG","活动Id"+(join.getImActivity().getObjectId() == null));
                    }
                    for (Join join : joins) {
                        DbUtil.getInstance().addJoin(join);
                    }
                    mvpView.loadMyJoin(DbUtil.getInstance()
                            .queryMyJoinImActivity(BmobUser
                                    .getCurrentUser(User.class)));
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.loadFail(error);
                    List<ImActivity> imActivities = DbUtil.getInstance()
                            .queryMyJoinImActivity(BmobUser.getCurrentUser(User.class));
                    if (imActivities.size() > 0){
                        mvpView.loadMyJoin(imActivities);
                    }
                    Log.i("TAG",error);
                }
            }
        });

    }


}
