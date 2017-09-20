package com.here.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.here.HereApplication;
import com.here.bean.ImageAddress;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hyc on 2017/7/3 03:18
 */

public class TinyUtil {

    public interface OnCompressListener{
        void success(String out);
        void fail();
    }

    public interface OnBatchCompressListener{
        void success(String[] out);
        void fail();
    }

    /**
     * 判断本地文件是否存在
     * @param path  文件地址
     * @return   是否存在
     */
    public static boolean isFileExists(String path){
        try {
            File file = new File(path);
            if (file.exists()){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断图片是否压缩过，如果压缩过，则返回压缩过后的图片地址
     * 此举为了避免图片压缩，节省资源
     * @param path 图片原始地址
     * @return   图片已经压缩后的地址，null为没有压缩过缓存地址
     */
    public static String isCompress(String path){
        ImageAddress address = DbUtil.getInstance()
                .queryImageAddress(path);
        if (address != null){
            if (isFileExists(address.getCompressAddress())){
                return address.getCompressAddress();
            }else {
                DbUtil.getInstance().deleteImageAddress(path);
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 压缩头像和背景图片
     * @param path 图片存储路径
     * @param listener 压缩监听
     */
    public static void compress(final String path
            , final OnCompressListener listener){
        Tiny.FileCompressOptions options = new Tiny
                .FileCompressOptions();
        options.width = 300;
        options.height = 300;
        Tiny.getInstance().source(path).asFile()
                .withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                if (isSuccess){
                    listener.success(outfile);
                }else {
                    listener.fail();
                }
            }
        });

    }

    /**
     * 压缩聊天图片，压缩过的不再重复压缩
     * @param path  图片存储路径
     * @param listener
     */
    public static void compressChatImage(final String path
            , final OnCompressListener listener){
        String compressAddress = isCompress(path);
        if (!TextUtils.isEmpty(compressAddress)){
            listener.success(compressAddress);
            return;
        }
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.quality = 30;
        Tiny.getInstance().source(path).asFile()
                .withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                if (isSuccess){
                    ImageAddress address = new ImageAddress();
                    address.setCompressAddress(outfile);
                    address.setOriginalAddress(path);
                    address.setCloudAddress("");
                    DbUtil.getInstance().addImageAddress(address);
                    listener.success(outfile);
                }else {
                    listener.fail();
                }
            }
        });
    }


    /**
     * 批量压缩图片
     * @param source  图片地址
     * @param lister  压缩监听
     */
    public static void batchCompress(String[] source
            , final OnBatchCompressListener lister){
        final List<String> data = new ArrayList<>();
        List<String> needCompress = new ArrayList<>();
        for (String s : source) {
            String path = isCompress(s);
            if (TextUtils.isEmpty(path)){
                needCompress.add(s);
            }else {
                data.add(path);
            }
        }
        if (needCompress.size() > 0){
            final String[] compressSource= new String[needCompress.size()];
            for (int i = 0; i < needCompress.size(); i++) {
                compressSource[i] = needCompress.get(i);
                Log.i("压缩图片",compressSource[i]);
            }

            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            options.quality=50;
            Tiny.getInstance().source(compressSource)
                    .batchAsFile().withOptions(options)
                    .batchCompress(new FileBatchCallback() {
                        @Override
                        public void callback(boolean isSuccess, String[] outfile) {
                            if (isSuccess){
                                for (int i = 0; i < outfile.length; i++) {
                                    data.add(outfile[i]);
                                    ImageAddress address = new ImageAddress();
                                    address.setOriginalAddress(compressSource[i]);
                                    address.setCompressAddress(outfile[i]);
                                    address.setCloudAddress("");
                                    DbUtil.getInstance().addImageAddress(address);
                                }
                                String[] complete = new String[data.size()];
                                for (int i = 0; i < data.size(); i++) {
                                    complete[i] = data.get(i);
                                }
                                lister.success(complete);
                            }else {
                                lister.fail();
                            }
                        }
                    });
        }else {
            String[] completeAddress = new String[data.size()];
            for (int i = 0; i < completeAddress.length; i++) {
                completeAddress[i] = data.get(i);
            }
            lister.success(completeAddress);
        }

    }

}
