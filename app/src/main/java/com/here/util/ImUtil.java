package com.here.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.here.bean.User;
import com.here.chat.ChatActivity;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/7/8 14:05
 */

public class ImUtil {

    public static boolean isConnected=false;

    public interface OnConnectListener{
        void connectSuccess();
        void connecting();
        void connectFail();
    }

    public static void connectServer(){
        initConnectListener();
        if (BmobUser.getCurrentUser(User.class) == null){
            return;
        }
        BmobIM.connect(BmobUser.getCurrentUser(User.class).getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                Log.i("TAG","调用");
                    if (e == null){
                        Log.i("TAG","服务器连接成功");
                    }else {
                        Log.i("连接",e.getMessage()+"  "+e.getErrorCode());
                    }
            }
        });

    }

    public static void initConnectListener(){
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                switch (status.getCode()){
                    //断开连接
                    case 0:
                        isConnected = false;
                        break;
                    //正在连接
                    case 1:
                        break;
                    //连接成功
                    case 2:
                        isConnected = true;
                        break;
                    //网络不可用
                    case -1:
                        isConnected = false;
                        break;
                    //另一台设备登录
                    case -2:
                        break;
                }
                Log.i("连接",status.getMsg() +"  "+status.getCode());
            }
        });
    }
}
