package com.here.record;

import com.here.base.BasePresenter;
import com.here.bean.ImActivity;
import com.here.bean.User;
import com.here.util.RecordUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/17 18:49
 */

public class RecordPresenter extends BasePresenter<RecordContract> {

    private List<ImActivity> publishs;

    private List<ImActivity> joins;

    public void loadMyPublish(){
        if (publishs == null){
            mvpView.showLoading();
            RecordUtil.queryMyPublish(BmobUser.getCurrentUser(User.class), new RecordUtil.OnQueryListener() {
                @Override
                public void success(List<ImActivity> imActivities) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        publishs = imActivities;
                        mvpView.LoadActivity(imActivities);
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.loadFail(error);
                    }
                }
            });
        }else {
            mvpView.LoadActivity(publishs);
        }

    }

    public void loadMyJoin(){
        if (joins == null){
            mvpView.showLoading();
            RecordUtil.queryMyJoin(BmobUser.getCurrentUser(User.class), new RecordUtil.OnQueryListener() {
                @Override
                public void success(List<ImActivity> imActivities) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        joins = imActivities;
                        mvpView.LoadActivity(imActivities);
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.loadFail(error);
                    }
                }
            });
        }else {
            mvpView.LoadActivity(joins);
        }

    }


}
