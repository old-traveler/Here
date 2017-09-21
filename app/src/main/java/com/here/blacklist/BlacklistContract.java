package com.here.blacklist;

import com.here.bean.User;

import java.util.List;

/**
 * Created by hyc on 2017/9/21 22:06
 */

public interface BlacklistContract {

    void showLoading();

    void stopLoading();

    void setBlacklist(List<User> user);

    void loadFail(String error);

}
