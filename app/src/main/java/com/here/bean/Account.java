package com.here.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by hyc on 2017/7/2 22:42
 */

public class Account  extends DataSupport{

    private String username;

    private String name;

    private boolean isThird;

    private String password;

    private String imageUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isThird() {
        return isThird;
    }

    public void setThird(boolean third) {
        isThird = third;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
