package com.here.bean;

import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/6/23 11:04
 */

public class User  extends BmobUser{

    private String name;

    private String nickname;

    private String sex;

    private int age;

    private String DateOfBirth;

    private String introduction;

    private String tips;

    private String headImageUrl;

    private String address;

    private boolean isShowNumber;

    private boolean isShowAge;

    private boolean isShowDataOfBirth;

    private String backgroundUrl;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isShowNumber() {
        return isShowNumber;
    }

    public void setShowNumber(boolean showNumber) {
        isShowNumber = showNumber;
    }

    public boolean isShowAge() {
        return isShowAge;
    }

    public void setShowAge(boolean showAge) {
        isShowAge = showAge;
    }

    public boolean isShowDataOfBirth() {
        return isShowDataOfBirth;
    }

    public void setShowDataOfBirth(boolean showDataOfBirth) {
        isShowDataOfBirth = showDataOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
}
