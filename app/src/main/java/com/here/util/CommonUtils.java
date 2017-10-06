package com.here.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.here.HereApplication;

import java.lang.reflect.Field;

public class CommonUtils {



    /**
     * 获取dimens定义的大小
     * @param dpValue
     * @return
     */
    public static int dipToPx(int dpValue) {
        final float scale = HereApplication.getContext()
                .getResources().getDisplayMetrics().density;
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

    /**
     * 作用如上，缩放详情界面的图片
     * @param width  原始宽度
     * @param height  原始高度
     * @return
     */
    public static int[] zoomImageDetail(int width , int height){
        int[] size = new int[2];
        if (width > height){
            if (width > dipToPx(250)){
                size[0] = dipToPx(250);
                size[1] = height * dipToPx(250) / width;
            }else {
                size[0] = width;
                size[1] = height;
            }
        }else {
            if (height > dipToPx(300)){
                size[0] = width * dipToPx(300) / height;
                size[1] = dipToPx(300);
            }else {
                size[0] = width;
                size[1] = height;
            }
        }
        return size;
    }


    public static int[] zoomCommunityImage(int width , int height){
        int[] size = new int[2];
        if (width > height){
            if (width > dipToPx(210)){
                size[0] = dipToPx(210);
                size[1] = height * dipToPx(210) / width;
            }else {
                size[0] = width;
                size[1] = height;
            }
        }else {
            if (height > dipToPx(250)){
                size[0] = width * dipToPx(250) / height;
                size[1] = dipToPx(250);
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

    public static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }




}
