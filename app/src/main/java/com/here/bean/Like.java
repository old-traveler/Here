package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/13 16:36
 */

public class Like extends BmobObject{

    private User user;

    private BmobObject publish;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobObject getPublish() {
        return publish;
    }

    public void setPublish(BmobObject publish) {
        this.publish = publish;
    }
}
