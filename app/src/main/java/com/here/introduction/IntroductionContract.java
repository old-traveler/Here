package com.here.introduction;

/**
 * Created by hyc on 2017/7/1 11:08
 */

public interface IntroductionContract {

    String getIntroduction();

    void showLoading();

    void stopLoading();

    void updateSuccess();

    void updateFail(String error);


}
