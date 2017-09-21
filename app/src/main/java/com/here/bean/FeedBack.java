package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/9/21 15:55
 */

public class FeedBack extends BmobObject{
    /**
     * 提交者
     */
    private User submitter;
    /**
     * 是否为程序错误
     */
    private boolean isError;
    /**
     * 反馈建议
     */
    private String feedBack;
    /**
     * 联系电话
     */
    private String number;

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
