package com.here.privacy;

import com.here.bean.User;

/**
 * Created by hyc on 2017/7/1 13:20
 */

public interface PrivacyContract {

    User getUpdateUser();

    void showLoading();

    void stopLoading();

    void update();

    void updateSuccess();

    void updateFail(String error);


}
