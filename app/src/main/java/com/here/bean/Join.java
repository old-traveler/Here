package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/10 18:48
 */

public class Join extends BmobObject {

    private ImActivity imActivity;

    private User joinUser;

    public ImActivity getImActivity() {
        return imActivity;
    }

    public void setImActivity(ImActivity imActivity) {
        this.imActivity = imActivity;
    }

    public User getJoinUser() {
        return joinUser;
    }

    public void setJoinUser(User joinUser) {
        this.joinUser = joinUser;
    }
}
