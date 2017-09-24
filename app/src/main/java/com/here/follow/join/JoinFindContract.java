package com.here.follow.join;

/**
 * Created by hyc on 2017/9/23 17:37
 */

public interface JoinFindContract {

    void showLoading();

    void stopLoading();

    void uploadImageSuccess(String path);

    void showTip(String msg);

    void selectImage();

    void hadJoin();

    void hadNoJoin();

}
