package com.here.phone;

/**
 * Created by hyc on 2017/6/24 11:13
 */

public interface PhoneContract {


    void changePhoneNumber();

    void verifyNumber();

    void showLoading();

    void stopLoading();

    void updateSuccess();

    void verifyFail(String error);

    void initNumber();



}
