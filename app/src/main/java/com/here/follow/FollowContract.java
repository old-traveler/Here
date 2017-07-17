package com.here.follow;

import com.here.bean.Community;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public interface FollowContract {

    void showLoading();

    void stopLoading();

    void loadSuccess(List<Community> communities);

    void loadFail(String error);
}
