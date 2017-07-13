package com.here.personal;

/**
 * Created by hyc on 2017/6/25 19:57
 */

public interface PersonalContract {

    void updateNickName();

    void updateAge();

    void updateSex();

    void updateAddress();

    void updateTips();

    void showLoading();

    void stopLoading();

    void updateFail(String error);

    void updateBirthday();

    void updateIntroduction();

    void updateEmail();

    void updatePrivacy();

    void showTheNumber();

    void updateBackground();

    void initUserData();

    void startImagePicker();

    void showTheBigHead();

    void background();

    void showTheBigBg();


}
