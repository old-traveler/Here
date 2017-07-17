package com.here.follow.info;

import com.here.bean.Follow;

import java.util.List;

/**
 * Created by hyc on 2017/7/16 23:38
 */

public interface FollowContract {

    void showLoading();

    void stopLoading();

    void loadMyFans(List<Follow> fans);

    void loadMyFollow(List<Follow> follow);

    void loadFail(String error);
}
