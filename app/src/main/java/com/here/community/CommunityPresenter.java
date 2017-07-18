package com.here.community;

import com.here.base.BasePresenter;
import com.here.bean.Community;
import com.here.util.CommunityUtil;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 15:00
 */

public class CommunityPresenter extends BasePresenter<CommunityContract> {

    /**
     * 加载社区数据
     */
    public void loadCommunityData(final boolean isRefesh){

        if (!isRefesh){
            mvpView.showLoading();
        }

        CommunityUtil.queryMood( new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                loadMoodData(communities,isRefesh);
            }

            @Override
            public void fail(String error) {
                if (mvpView !=null){
                    if (!isRefesh){
                        mvpView.stopLoading();
                    }
                    mvpView.fail(error);
                }

            }
        });
    }

    /**
     * 加载心情分享数据
     * @param communityList
     */
    public void loadMoodData(final List<Community> communityList, final boolean isRefresh){
        CommunityUtil.queryAppointment(new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                for (Community community : communityList) {
                    communities.add(community);
                }
                if (mvpView != null){
                    mvpView.setRecommend(CommunityUtil.sortByTime(communities));
                    if (!isRefresh){
                        mvpView.stopLoading();
                    }
                }

            }

            @Override
            public void fail(String error) {
                if (mvpView != null){
                    mvpView.setRecommend(CommunityUtil.sortByTime(communityList));
                    if (!isRefresh){
                        mvpView.stopLoading();
                    }
                    mvpView.fail(error);
                }
            }
        });
    }


}
