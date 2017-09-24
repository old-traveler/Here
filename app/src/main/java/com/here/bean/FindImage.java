package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/9/23 15:22
 */

public class FindImage extends BmobObject{

    private User master;

    private String url;

    private int width;

    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
