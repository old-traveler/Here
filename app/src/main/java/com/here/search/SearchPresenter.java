package com.here.search;

import android.text.TextUtils;

import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.UserUtil;

/**
 * Created by hyc on 2017/7/10 18:55
 */

public class SearchPresenter extends BasePresenter<SearchContract> {

    public void searchUser(){
        if (!TextUtils.isEmpty(mvpView.getSearchInfo())){
            mvpView.showLoading();
            UserUtil.searchUserInfo(mvpView.getSearchInfo(), new UserUtil.OnSearchUserListener() {
                @Override
                public void success(User user) {
                    mvpView.stopLoading();
                    mvpView.loadingSuccess(user);
                }

                @Override
                public void fail(String error) {
                    mvpView.stopLoading();
                    mvpView.fail(error);
                }
            });
        }else {
            mvpView.fail("用户名不能为空");
        }
    }


}
