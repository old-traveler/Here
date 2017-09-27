package com.here.personal.accusation;

import com.here.bean.User;

/**
 * Created by hyc on 2017/9/27 22:26
 */

public interface AccusationContract {
    void showLoading();

    void stopLoading();

    void submitSuccess();

    void showTips(String error);

    User getUser();
}
