package com.here.password;

import android.text.TextUtils;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.util.UserUtil;

/**
 * Created by hyc on 2017/7/2 19:44
 */

public class PasswordPresenter extends BasePresenter<PasswordContract> {

    public void updatePassword(){
        if (!TextUtils.isEmpty(mvpView.getOldPassword())&&!TextUtils.isEmpty(mvpView.getNewPassword())){
            mvpView.showLoading();
            UserUtil.updatePassword(mvpView.getOldPassword(), mvpView.getNewPassword(), new UserUtil.OnDealListener() {
                @Override
                public void success() {
                    mvpView.stopLoading();
                    mvpView.updateSuccess();
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.updateFail(error);
                }
            });
        }else {
            mvpView.updateFail(HereApplication.getContext().getString(R.string.password_no_fill));
        }
    }

}
