package com.here.main;

import com.here.base.BasePresenter;

/**
 * Created by hyc on 2017/6/21 14:17
 */

public class MainPresenter extends BasePresenter<MainContract> {

    public static boolean isUpdate=false;

    public void updateUserInfo(){
        if (isUpdate){
            isUpdate=false;
            mvpView.initUserData();
        }
    }

}
