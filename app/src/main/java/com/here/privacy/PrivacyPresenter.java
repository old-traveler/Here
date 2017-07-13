package com.here.privacy;

import com.here.base.BasePresenter;
import com.here.util.UserUtil;

/**
 * Created by hyc on 2017/7/1 13:19
 */

public class PrivacyPresenter extends BasePresenter<PrivacyContract> {

    public void update(){
        mvpView.showLoading();
        UserUtil.updateUserInfo(mvpView.getUpdateUser(), new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.updateSuccess();
                mvpView.stopLoading();
            }

            @Override
            public void fail(String error) {
                mvpView.updateFail(error);
                mvpView.stopLoading();
            }
        });
    }


}
