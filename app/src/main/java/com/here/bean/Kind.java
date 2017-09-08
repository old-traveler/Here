package com.here.bean;

/**
 * Created by hyc on 2017/9/8 11:24
 */

public class Kind {
    /**
     * 类型名
     */
    private String name;
    /**
     * 类型背景图片
     */
    private int imgId;

    public Kind(int imgId,String name ){
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
