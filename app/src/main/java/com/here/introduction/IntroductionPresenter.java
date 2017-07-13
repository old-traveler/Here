package com.here.introduction;

import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.UserUtil;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/1 11:08
 */

public class IntroductionPresenter  extends BasePresenter<IntroductionContract> {

    public void updateIntroduction(){
        if (!mvpView.getIntroduction().equals(BmobUser.getCurrentUser(User.class).getIntroduction())){
            mvpView.showLoading();
            User user = BmobUser.getCurrentUser(User.class);
            user.setIntroduction(mvpView.getIntroduction());
            UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
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
            mvpView.updateSuccess();
        }
    }

}
