package com.here.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/9/27 21:50
 */

public class Report extends BmobObject{

    private User reporter;

    private String contractNumber;

    private String content;

    private String kind;

    private ImActivity imActivity;

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
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

    public ImActivity getImActivity() {
        return imActivity;
    }

    public void setImActivity(ImActivity imActivity) {
        this.imActivity = imActivity;
    }
}
