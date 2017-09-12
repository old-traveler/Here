package com.here.binding;

import com.here.bean.User;

/**
 * Created by hyc on 2017/9/8 14:44
 */

public interface BindInfoContract {

    void showUserDeal();

    boolean isAgreeDeal();

    boolean isFilled();

    boolean idIsRight();

    User getUser();

    void showTips(int msg);

    void showLoading();

    void stopLoading();

    void bindSuccess();

    void bindFail(String error);
}
