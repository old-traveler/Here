package com.here.follow;

import android.util.Log;

import com.here.base.BasePresenter;
import com.here.bean.Community;
import com.here.bean.FindImage;
import com.here.util.CommunityUtil;
import com.here.util.FindUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public class FollowPresenter extends BasePresenter<FollowContract> {

    private int hadJoin = -1;

    public void hadJoin(){
        if (hadJoin == -1){
            FindUtil.isJoinFind(new FindUtil.OnFindListener() {
                @Override
                public void hadJoin() {
                    hadJoin = 1;
                }

                @Override
                public void noJoin() {
                    hadJoin = 0;
                    mvpView.reminderJoin();
                }
            });
        }else if (hadJoin == 0){
            mvpView.reminderJoin();
        }

    }

    public void refuse() {
        hadJoin = 1;
        FindUtil.addRecordCache("","refuse");
    }

    public void load(final int page){
        FindUtil.queryFinds(page, new FindUtil.OnQueryListener() {
            @Override
            public void onSuccess(List<FindImage> findImages) {
                if (mvpView!= null && page == 0){
                    mvpView.refreshData(findImages);
                }else if (mvpView!=null){
                    mvpView.addData(findImages);
                }
            }

            @Override
            public void onError(String error) {
                if (mvpView != null){
                    mvpView.loadFail(error);
                }
            }
        });
    }


//    public void queryAppointment(final boolean isRefresh, final RefreshLayout refreshLayout){
//        if (!isRefresh){
//            mvpView.showLoading();
//        }
//        CommunityUtil.queryFollowMood(new CommunityUtil.CommunitySearchListener() {
//            @Override
//            public void success(List<Community> communities) {
//                if (mvpView == null){
//                    return;
//                }
//                queryMood(communities,isRefresh,refreshLayout);
//            }
//
//            @Override
//            public void fail(String error) {
//                if (mvpView == null){
//                    return;
//                }
//                if (!isRefresh){
//                    mvpView.stopLoading();
//                }else {
//                    refreshLayout.finishRefresh();
//                }
//                mvpView.loadFail(error);
//            }
//        });
//    }
//
//
//
//    public void queryMood(final List<Community> communityList, final boolean isRefresh, final RefreshLayout refreshLayout){
//        CommunityUtil.queryFollowAppointment(new CommunityUtil.CommunitySearchListener() {
//            @Override
//            public void success(List<Community> communities){
//                if (mvpView == null){
//                    return;
//                }
//                if (communityList !=null){
//                    if (communities == null){
//                        mvpView.loadSuccess(communityList);
//                    }else {
//                        for (Community community : communityList) {
//                            communities.add(community);
//                        }
//                        mvpView.loadSuccess(communities);
//                    }
//                    if (!isRefresh){
//                        mvpView.stopLoading();
//                    }else {
//                        refreshLayout.finishRefresh(0);
//                    }
//
//
//                }else {
//                    if (communities == null){
//                        mvpView.loadFail("没有数据");
//                    }else {
//                        mvpView.loadSuccess(communities);
//                    }
//                    if (!isRefresh){
//                        mvpView.stopLoading();
//                    }else {
//                        refreshLayout.finishRefresh(0);
//                    }
//                }
//            }
//
//            @Override
//            public void fail(String error) {
//                if (mvpView == null){
//                    return;
//                }
//                if (!isRefresh){
//                    mvpView.stopLoading();
//                }
//                mvpView.loadFail(error);
//            }
//        });
//    }

}
