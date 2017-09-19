package com.here.util;

import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.ImageAddress;

import java.util.ArrayList;
import java.util.List;

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


    public static void uploadBatch(final String[] path, final OnUploadBatchListener listener){
        final List<String> data = new ArrayList<>();
        List<String> needUploads = new ArrayList<>();
        for (String s : path) {
            ImageAddress imageAddress= DbUtil.getInstance().queryImageAddressByCompressAddress(s);
            if (imageAddress != null && !TextUtils.isEmpty(imageAddress.getCloudAddress())){
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
                            DbUtil.getInstance().updateImageAddress(upload[i],list1.get(i));
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
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(i+s);
                    }
                }
            });
        }else {
            listener.success(data);
        }
    }


}
