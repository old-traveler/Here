package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/9/27 22:36
 */

public class Accusation extends BmobObject{

    private User submiter;

    private String content;

    private String kind;

    private String number;

    private User user;

    public User getSubmiter() {
        return submiter;
    }

    public void setSubmiter(User submiter) {
        this.submiter = submiter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
