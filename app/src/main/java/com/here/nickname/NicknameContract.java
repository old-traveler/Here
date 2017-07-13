package com.here.nickname;

/**
 * Created by hyc on 2017/7/1 12:16
 */

public interface NicknameContract {

    String getNickname();

    void showLoading();

    void stopLoading();

    void updateSuccess();

    void updateFail(String error);

}
