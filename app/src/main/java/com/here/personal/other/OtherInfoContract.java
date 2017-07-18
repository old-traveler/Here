package com.here.personal.other;

import com.here.bean.User;

/**
 * Created by hyc on 2017/7/18 10:59
 */

public interface OtherInfoContract {

    User getUserInfo();

    void showLoading();

    void stopLoading();

    void fail(String error);

    void setUserInfo(User user);

}
