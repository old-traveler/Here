package com.here.util;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImageAddress;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by hyc on 2017/7/4 17:30
 */

public class FileUtil {

    public interface OnUploadBatchListener{
        void success(List<String> images);
        void fail(String error);
    }

    /**
     * 批量上传图片，上传过的图片直接返回云端地址
     * @param path     图片路径
     * @param listener 上传监听
     */
    public static void uploadBatch(final String[] path
            , final OnUploadBatchListener listener){
        final List<String> data = new ArrayList<>();
        List<String> needUploads = new ArrayList<>();
        for (String s : path) {
            ImageAddress imageAddress= DbUtil.getInstance()
                    .queryImageAddressByCompressAddress(s);
            if (imageAddress != null && !TextUtils.isEmpty(
                    imageAddress.getCloudAddress())){
                data.add(imageAddress.getCloudAddress());
            }else {
                needUploads.add(s);
            }
        }

        if (needUploads.size() > 0){
            final String[] upload =new String[needUploads.size()];
            for (int i = 0; i < needUploads.size(); i++) {
                upload[i] = needUploads.get(i);
            }
            BmobFile.uploadBatch(upload, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (upload.length==list1.size()){
                        for (int i = 0; i < list1.size(); i++) {
                            data.add(list1.get(i));
                            DbUtil.getInstance().updateImageAddress(
                                    upload[i],list1.get(i));
                        }
                        listener.success(data);
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    if (i==9016){
                        listener.fail(HereApplication.getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        listener.fail(i+s);
                    }
                }
            });
        }else {
            listener.success(data);
        }
    }

    public static Map<String,Object> getFileInfo(String path){
        Map<String, Object> attr = new HashMap();
        File file;
        File imageFile = file = new File(path);
        BitmapFactory.Options options;
        (options = new BitmapFactory.Options()).inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int width = options.outWidth;
        int height = options.outHeight;
        String format = options.outMimeType;
        HashMap var5;
        (var5 = new HashMap()).put("format", format);
        var5.put("width", Integer.valueOf(width));
        var5.put("height", Integer.valueOf(height));
        var5.put("size", Long.valueOf(file.length()));
        attr.put("metaData", var5);
        return attr;
    }


}
