package com.here.record.publish;

import com.here.base.BasePresenter;
import com.here.bean.Community;
import com.here.bean.ImActivity;
import com.here.util.CommunityUtil;
import com.here.util.ImActivityUtil;


import java.util.List;

/**
 * Created by hyc on 2017/9/24 11:32
 */

public class PublishRecordPresenter extends BasePresenter<PublishRecordContract>{

    /**
     * 查询用户的心情分享记录，并添加到界面中显示
     * @param page   查询页码
     */
    public void queryShareRecord(final int page){
        CommunityUtil.queryUserMoodRecord(mvpView.getPublisher()
                , page, new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView == null) return;
                if (page == 0){
                    mvpView.setShareRecord(communities);
                }else {
                    mvpView.addShareRecord(communities);
                }
            }

            @Override
            public void fail(String error) {

            }
        });
    }

    /**
     * 查询用户发布的预约活动记录，并添加到界面中显示
     * @param page   查询页码
     */
    public void queryAppointmentRecord(final int page){
        CommunityUtil.queryUserAppointmentRecord(mvpView.getPublisher()
                ,page, new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView == null) return;
                if (page == 0){
                    mvpView.setAppointmentRecord(communities);
                }else {
                    mvpView.addAppointmentRecord(communities);
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView!=null){
                    mvpView.loadFail(error);
                }
            }
        });
    }


    /**
     * 查询用户发布的即时活动记录，并添加到界面中显示
     * @param page   查询页码
     */
    public void queryImActivityRecord(final int page){
        ImActivityUtil.queryUserImActivityRecord(mvpView.getPublisher()
                , page, new ImActivityUtil.OnGetNearByListener() {
            @Override
            public void success(List<ImActivity> activities) {
                if (mvpView == null) return;
                if (page == 0){
                    mvpView.setImActivityRecord(activities);
                }else {
                    mvpView.addImActivityRecord(activities);
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView!=null){
                    mvpView.loadFail(error);
                }
            }
        });

    }



}
