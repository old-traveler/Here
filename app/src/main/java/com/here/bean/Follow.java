package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/13 16:46
 */

public class Follow extends BmobObject{

    private User user;

    private User followUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowUser() {
        return followUser;
    }

    public void setFollowUser(User followUser) {
        this.followUser = followUser;
    }
}
