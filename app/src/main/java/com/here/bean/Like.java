package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/13 16:36
 */

public class Like extends BmobObject{

    private User user;

    private String publishId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPublish() {
        return publishId;
    }

    public void setPublish(String publish) {
        this.publishId = publish;
    }
}
