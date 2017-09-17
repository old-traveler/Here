package com.here.follow.info;

import com.here.bean.Follow;
import com.here.bean.User;

import java.util.List;

/**
 * Created by hyc on 2017/7/16 23:38
 */

public interface FollowContract {

    void showLoading();

    void stopLoading();

    void loadMyFans(List<User> fans);

    void loadMyFollow(List<User> User);

    void loadFail(String error);
}
