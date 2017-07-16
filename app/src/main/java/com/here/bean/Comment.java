package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/13 16:38
 */

public class Comment extends BmobObject{

    private User user;

    private String publish;

    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
