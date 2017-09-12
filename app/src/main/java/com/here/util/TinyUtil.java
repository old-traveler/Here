package com.here.util;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import com.zxy.tiny.callback.FileCallback;

import java.lang.reflect.Array;
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



    public static void compress(String path, final OnCompressListener listener){
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.width = 300;
        options.height = 300;
        Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
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

    public static void compressChatImage(String path, final OnCompressListener listener){
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.quality = 30;
        Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
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

    public static void batchCompress(String[] source, final OnBatchCompressListener lister){
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.quality=50;
        Tiny.getInstance().source(source).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile) {
                if (isSuccess){
                    lister.success(outfile);
                }
            }
        });
    }

}
