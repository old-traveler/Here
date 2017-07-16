package com.here.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by hyc on 2017/7/13 16:42
 */

public class LikeId extends DataSupport {

    private String likeId;

    private String publishId;

    private String userId;

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
