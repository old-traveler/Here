package com.here.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by hyc on 2017/7/13 16:48
 */

public class FollowId  extends DataSupport{

    private String followId;

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    private String followUserId;

    public String getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(String followUserId) {
        this.followUserId = followUserId;
    }
}
