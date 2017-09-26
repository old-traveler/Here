package com.here.community.details;

import com.here.base.BasePresenter;
import com.here.bean.Community;
import com.here.util.CommunityUtil;

import java.util.List;

/**
 * Created by hyc on 2017/7/14 08:06
 */

public class CommunityDetailsPresenter  extends BasePresenter<CommunityDetailsContract>{

    /**
     * 加载社区数据
     */
    public void loadCommunityData(final int page){
        if (page == 0){
            mvpView.showLoading();
        }
        CommunityUtil.queryAppointmentByKind(mvpView.getKind()
                , page,new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView == null){
                    return;
                }
                if (page == 0){
                    mvpView.stopLoading();
                    mvpView.setAppointment(communities);
                }else {
                    mvpView.addAppointment(communities);
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView == null){
                    return;
                }
                if (page == 0){
                    mvpView.stopLoading();
                }
                mvpView.fail(error);
            }
        });
    }

    /**
     * 加载心情分享数据

     */
    public void loadMoodData(final int page){
        if (page == 0){
            mvpView.showLoading();
        }
        CommunityUtil.queryMoodByKind(mvpView.getKind(),page, new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView == null){
                    return;
                }
                if (page == 0){
                    mvpView.stopLoading();
                    mvpView.setShare(communities);
                }else {
                    mvpView.addShare(communities);
                }

            }

            @Override
            public void fail(String error) {
                if (mvpView == null){
                    return;
                }
                if (page == 0){
                    mvpView.stopLoading();
                }
                mvpView.fail(error);
            }
        });
    }


}
