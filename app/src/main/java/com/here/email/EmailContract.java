package com.here.email;

/**
 * Created by hyc on 2017/7/1 12:24
 */

public interface EmailContract {

    String getEmail();

    void showLoading();

    void stopLoading();

    void updateSuccess();

    void updateFail(String error);



}
