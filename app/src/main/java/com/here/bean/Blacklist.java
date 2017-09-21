package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/9/21 20:50
 */

public class Blacklist extends BmobObject{

    private User user;

    private User blacklistUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getBlacklistUser() {
        return blacklistUser;
    }

    public void setBlacklistUser(User blacklistUser) {
        this.blacklistUser = blacklistUser;
    }
}
