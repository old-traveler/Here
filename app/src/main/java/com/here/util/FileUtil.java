package com.here.util;

import com.here.HereApplication;
import com.here.R;

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

        BmobFile.uploadBatch(path, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (path.length==list1.size()){
                    listener.success(list1);
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
    }


}
