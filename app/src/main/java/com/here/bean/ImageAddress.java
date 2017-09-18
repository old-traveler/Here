package com.here.bean;

/**
 * 图片存储信息类 记录图片存储信息，以便于减少图片重复压缩 ，重复上传，减少资源的使用 Created by hyc on 2017/9/18 21:58
 */

public class ImageAddress {
    /**
     * 图片的原始地址
     */
    private String originalAddress;
    /**
     * 图片压缩后的地址
     */
    private String compressAddress;
    /**
     * 图片对应的云端下载地址
     */
    private String cloudAddress;

    public String getOriginalAddress() {
        return originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public String getCompressAddress() {
        return compressAddress;
    }

    public void setCompressAddress(String compressAddress) {
        this.compressAddress = compressAddress;
    }

    public String getCloudAddress() {
        return cloudAddress;
    }

    public void setCloudAddress(String cloudAddress) {
        this.cloudAddress = cloudAddress;
    }
}
