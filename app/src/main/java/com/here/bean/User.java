package com.here.bean;

import java.io.Serializable;
import cn.bmob.v3.BmobUser;

/**
 * 用户类 Created by hyc on 2017/6/23 11:04
 */

public class User  extends BmobUser implements Serializable{
    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private int age;
    /**
     * 生日
     */
    private String DateOfBirth;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 标签
     */
    private String[] tips;
    /**
     * 头像地址
     */
    private String headImageUrl;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 家庭住址
     */
    private String homeAddress;
    /**
     * 是否向其他用户展示电话号码
     */
    private boolean isShowNumber;
    /**
     * 是否向其他用户展示年龄
     */
    private boolean isShowAge;
    /**
     * 是否向其他用户展示生日日期
     */
    private boolean isShowDataOfBirth;
    /**
     * 用户背景图片地址
     */
    private String backgroundUrl;
    /**
     * 身份证号
     */
    private String idCard;


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

    public String[] getTips() {
        return tips;
    }

    public void setTips(String[] tips) {
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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
}
