package com.here.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.here.HereApplication;

/**
 * Created by hyc on 2017/9/27 13:57
 */

public class SettingUtil {

    private static SettingUtil settingUtil;

    public static synchronized SettingUtil getInstance(){
        return settingUtil == null ? new SettingUtil() : settingUtil;
    }

    /**
     * 设置消息通知是否声音提示
     * @param isVoice true则开启声音提示 false关闭
     */
    public void settingVoice(boolean isVoice){
        SharedPreferences setting = HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("voice",isVoice);
        editor.commit();
    }

    /**
     * 获取是否声音提示缓存
     * @return true 则消息声音提示
     */
    public boolean isVoice(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("voice",true);
    }

    /**
     * 设置消息通知是否震动提示
     * @param isVibration true则开启 false关闭
     */
    public void settingVibration(boolean isVibration){
        SharedPreferences setting = HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("vibration",isVibration);
        editor.commit();
    }

    /**
     * 获取是否震动提示缓存
     * @return true 则震动提示
     */
    public boolean isVibration(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("vibration",false);
    }

    /**
     * 设置消息通知是否通知栏显示
     * @param isShow true则开启 false关闭
     */
    public void settingNotice(boolean isShow){
        SharedPreferences setting = HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("notice_is_show",isShow);
        editor.commit();
    }

    /**
     * 获取是否通知栏显示
     * @return true则通知栏显示
     */
    public boolean isShowNotice(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("notice_is_show",false);
    }

    /**
     * 设置消息通知是否指示灯显示
     * @param isShow true则开启 false关闭
     */
    public void settingIndicator(boolean isShow){
        SharedPreferences setting = HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("indicator_is_show",isShow);
        editor.commit();
    }

    /**
     * 获取是否指示灯显示
     * @return true则指示灯显示
     */
    public boolean isShowIndicator(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("indicator_is_show",false);
    }

    /**
     * 设置退出程序后是否继续接收消息提醒
     * @param isReceive true则接收，false不接收
     */
    public void settingReceiveAtExit(boolean isReceive){
        SharedPreferences setting = HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("receive_at_exit",isReceive);
        editor.commit();
    }

    /**
     * 获取是否退出程序后接收消息
     * @return true则接收
     */
    public boolean isReceiveAtExit(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("receive_at_exit",false);
    }





}
