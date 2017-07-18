package com.here.personal.other;

import com.here.base.BasePresenter;

/**
 * Created by hyc on 2017/7/18 10:58
 */

public class OtherInfoPresenter extends BasePresenter<OtherInfoContract> {

    public void load(){
        mvpView.setUserInfo(mvpView.getUserInfo());
    }

}
