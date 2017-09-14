package com.here.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.here.HereApplication;

public class CommonUtils {



    /**
     * 获取dimens定义的大小
     * @param dpValue
     * @return
     */
    public static int dipToPx(int dpValue) {
        final float scale = HereApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 通过图片宽高信息按比例缩小
     * @param width  原始的图片的宽度
     * @param height 原始图片的高度
     * @return
     * size[0] 表示缩小后图片的宽度
     * size[1] 表示缩小后图片的高度
     */
    public static int[] zoomImage(int width , int height){
        int[] size = new int[2];
        if (width > height){
            if (width > dipToPx(180)){
                size[0] = dipToPx(180);
                size[1] = height * dipToPx(180) / width;
            }else {
                size[0] = width;
                size[1] = height;
            }
        }else {
            if (height > dipToPx(180)){
                size[0] = width * dipToPx(180) / height;
                size[1] = dipToPx(180);
            }else {
                size[0] = width;
                size[1] = height;
            }
        }
        return size;
    }

    public static Bundle captureValues(@NonNull View view) {
        Bundle b = new Bundle();
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        b.putInt("left", screenLocation[0]);
        b.putInt("top", screenLocation[1]);
        b.putInt("width", view.getWidth());
        b.putInt("height", view.getHeight());
        return b;
    }




}
