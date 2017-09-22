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
    public void loadCommunityData(final int page){
        CommunityUtil.queryMood(page, new CommunityUtil
                .CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView!=null && page == 0){
                    mvpView.setRecommend(communities);
                }else if (mvpView != null){
                    mvpView.addRecommend(communities);
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView !=null){
                    mvpView.fail(error);
                }
            }
        });
    }

    /**
     * 加载心情分享数据
     */
    public void loadMoodData(int page){
        CommunityUtil.queryAppointment(page, new CommunityUtil
                .CommunitySearchListener() {
            @Override
            public void success(List<Community> communities) {
                if (mvpView != null){
                    mvpView.setRecommend(communities);
                }

            }

            @Override
            public void fail(String error) {
                if (mvpView != null){
                    mvpView.fail(error);
                }
            }
        });
    }


}
