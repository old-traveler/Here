package com.here.community;

import com.here.bean.Community;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 15:00
 */

public interface CommunityContract {

    void showLoading();

    void stopLoading();

    void setRecommend(List<Community> communities);

    void fail(String error);




}
