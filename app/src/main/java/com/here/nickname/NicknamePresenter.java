package com.here.nickname;

import android.text.TextUtils;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.UserUtil;
import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/1 12:16
 */

public class NicknamePresenter extends BasePresenter<NicknameContract> {

    public void updateNickname(){
        if (!TextUtils.isEmpty(mvpView.getNickname())){
            mvpView.showLoading();
            User user = BmobUser.getCurrentUser(User.class);
            if (mvpView.getNickname().equals(user.getNickname())){
                mvpView.updateSuccess();
                return;
            }
            user.setNickname(mvpView.getNickname());
            UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
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
        }else {
            mvpView.updateFail(HereApplication.getContext().getString(R.string.nickname_empty));
        }

    }



}
