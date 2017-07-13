package com.here.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;
import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/4 12:36
 */

public class ImActivity  extends BmobObject implements Serializable{

    private User publisher;

    private String title;

    private String describe;

    private String[] images;

    private String location;

    private double longitude;

    private double latitude;

    private String overTime;

    private int number;

    private String publishDate;

    private String kind;

    private String publishTime;

    private int CurrentTime;

    private boolean isNeedApply;

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public boolean isNeedApply() {
        return isNeedApply;
    }

    public void setNeedApply(boolean needApply) {
        isNeedApply = needApply;
    }

    public boolean isEmpty(){
        return TextUtils.isEmpty(title)||TextUtils.isEmpty(describe)
                ||TextUtils.isEmpty(location)||TextUtils.isEmpty(overTime)||TextUtils.isEmpty(kind);
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(int currentTime) {
        CurrentTime = currentTime;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
