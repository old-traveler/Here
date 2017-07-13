package com.here.going;

import com.here.base.BasePresenter;
import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;
import com.here.util.ImActivityUtil;
import com.here.util.JoinUtil;
import com.here.util.TimeUtil;
import com.here.util.UserUtil;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/5 17:29
 */

public class GoingPresenter extends BasePresenter<GoingContract> {

    private User publisher;

    private int joinNumber = 0;

    private ImActivity imActivity;

    public void initGoing(){
        if (imActivity == null){
            imActivity = ImActivityUtil.getImActivityInfo(BmobUser.getCurrentUser(User.class));
        }

        mvpView.setGoingData(imActivity);
        Calendar now = Calendar.getInstance();
        int hour  = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int surplus = TimeUtil.countMinute(imActivity.getPublishTime(),hour+":"+minute);
        int total=TimeUtil.countMinute(imActivity.getPublishTime(),imActivity.getOverTime().split("-")[3]);
        mvpView.setSurplusTime((int)(((total-surplus)*1.00f/(total*1.00f))*100),surplus);
        mvpView.showLoading();
        JoinUtil.queryJoinUser(imActivity, new JoinUtil.OnSearchJoinListener() {
            @Override
            public void success(List<Join> joins) {
                joinNumber = joins.size();
                mvpView.loadJoinUserInfo(joins,imActivity);
                loadPublisherData();
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.fail(error);
            }
        });


    }

    public void loadPublisherData(){
        if ( !mvpView.isSelf() && publisher == null){
            UserUtil.searchUserInfoById(ImActivityUtil.getPublisherId(), new UserUtil.OnSearchUserListener() {
                @Override
                public void success(User user) {
                    mvpView.stopLoading();
                    publisher = user;
                    mvpView.loadPhoneNumber(user.getMobilePhoneNumber());
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.fail(error);
                }
            });
        }else {
            mvpView.stopLoading();
        }
    }

    public void refresh(){
        initGoing();
    }

    public void delete(){
        if (joinNumber == 0){
            mvpView.showLoading();
            ImActivityUtil.deleteImActivity(imActivity, new UserUtil.OnDealListener() {
                @Override
                public void success() {
                    mvpView.stopLoading();
                    mvpView.deleteSuccess();
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.fail(error);
                }
            });
        }else {
            mvpView.fail("已有伙伴加入，无法删除");
        }
    }

    public void contract(){

    }

}
