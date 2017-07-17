package com.here.follow;

import android.util.Log;

import com.here.base.BasePresenter;
import com.here.bean.Community;
import com.here.util.CommunityUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public class FollowPresenter extends BasePresenter<FollowContract> {

    public void queryAppointment(final boolean isRefresh, final RefreshLayout refreshLayout){
        if (!isRefresh){
            mvpView.showLoading();
        }
        CommunityUtil.queryFollowMood(new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView == null){
                    return;
                }
                queryMood(communities,isRefresh,refreshLayout);
            }

            @Override
            public void fail(String error) {
                if (mvpView == null){
                    return;
                }
                if (!isRefresh){
                    mvpView.stopLoading();
                }else {
                    refreshLayout.finishRefresh(0);
                }
                mvpView.loadFail(error);
            }
        });
    }



    public void queryMood(final List<Community> communityList, final boolean isRefresh, final RefreshLayout refreshLayout){
        CommunityUtil.queryFollowAppointment(new CommunityUtil.CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView == null){
                    return;
                }
                mvpView.stopLoading();
                if (communityList !=null){
                    if (communities == null){
                        mvpView.loadSuccess(communityList);
                    }else {
                        for (Community community : communityList) {
                            communities.add(community);
                        }
                        mvpView.loadSuccess(communities);
                    }
                    if (!isRefresh){
                        mvpView.stopLoading();
                    }else {
                        refreshLayout.finishRefresh(0);
                    }


                }else {
                    if (communities == null){
                        mvpView.loadFail("没有数据");
                    }else {
                        mvpView.loadSuccess(communities);
                    }
                    if (!isRefresh){
                        mvpView.stopLoading();
                    }else {
                        refreshLayout.finishRefresh(0);
                    }
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView == null){
                    return;
                }
                if (!isRefresh){
                    mvpView.stopLoading();
                }
                mvpView.loadFail(error);
            }
        });
    }

}
