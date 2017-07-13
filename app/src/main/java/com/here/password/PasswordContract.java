package com.here.password;

/**
 * Created by hyc on 2017/7/2 19:44
 */

public interface PasswordContract {

    String getOldPassword();

    String getNewPassword();

    void showLoading();

    void stopLoading();

    void updateSuccess();

    void updateFail(String error);


}
