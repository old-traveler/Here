package com.here.bean;

/**
 * Created by hyc on 2017/7/5 13:47
 */

public class Tip {

    private String name;

    private String slogan;

    private int  color;

    private boolean isHave=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isHave() {
        return isHave;
    }

    public void setHave(boolean have) {
        isHave = have;
    }
}
