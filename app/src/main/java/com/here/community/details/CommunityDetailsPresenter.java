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
    public void loadCommunityData(){
        mvpView.showLoading();
        CommunityUtil.queryAppointmentByKind(mvpView.getKind(), new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                loadMoodData(communities);
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.fail(error);
            }
        });
    }

    /**
     * 加载心情分享数据
     * @param communityList
     */
    public void loadMoodData(final List<Community> communityList){
        CommunityUtil.queryMoodByKind(mvpView.getKind(), new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                for (Community community : communityList) {
                    communities.add(community);
                }
                mvpView.loadSuccess(CommunityUtil.sortByTime(communities));
                mvpView.stopLoading();
            }

            @Override
            public void fail(String error) {
                mvpView.loadSuccess(CommunityUtil.sortByTime(communityList));
                mvpView.stopLoading();
                mvpView.fail(error);
            }
        });
    }


}
