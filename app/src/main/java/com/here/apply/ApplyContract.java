package com.here.apply;

import com.here.bean.User;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by hyc on 2017/7/11 20:04
 */

public interface ApplyContract {

    void fail(String error);

    void showLoading();

    void stopLoading();

    MessageEvent getApplyUserInfo();

    void respondSuccess(String msg);

    void loadUserInfo(BmobIMUserInfo info, String username);

}
