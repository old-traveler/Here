package com.here.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.User;
import com.here.chat.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
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
    public static void disconnect(){
        if (isConnected){
            isConnected = false;
            BmobIM.getInstance().disConnect();
        }
    }

    /**
     * 更新会话的信息
     * @param event
     */
    public static void updateUserInfo(MessageEvent event){
        final BmobIMConversation conversation = event.getConversation();
        if(conversation.getConversationId().equals(conversation.getConversationTitle())) {
            UserUtil.searchUserInfoById(conversation.getConversationId(), new UserUtil.OnSearchUserListener() {
                @Override
                public void success(User user) {
                    conversation.setConversationIcon(user.getHeadImageUrl());
                    if (TextUtils.isEmpty(user.getNickname())) {
                        conversation.setConversationTitle(user.getUsername());
                    } else {
                        conversation.setConversationTitle(user.getNickname());
                    }
                    BmobIM.getInstance().updateConversation(conversation);
                }

                @Override
                public void fail(String error) {

                }
            });
        }
    }

    public static List<Integer> getEmotions(Context context){
        List<Integer> list = new ArrayList<>();
        String[] emotionId;
        emotionId = context.getResources().getStringArray(R.array.emoji);
        for (String s : emotionId) {
            list.add(context.getResources().getIdentifier("emoji_"
                    +s,"drawable",context.getPackageName()));
        }
        return list;
    }

    public static void textViewLoadEmotion(Context context,String text ,TextView textView){
        int start = 0;
        int index = text.indexOf("emoji_");
        textView.setText(index == -1 ? text : "");
        while(index != -1){
            if (text.length() > index+7 && isDigit(text.substring(index+6,index+8))){
                textView.append(text.substring(start,index));
                start = index + 8;
                int drawable = context.getResources().getIdentifier(text
                        .substring(index,index+8),"drawable",context.getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),drawable);
                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                SpannableString spannableString=new SpannableString(text.substring(index,index+8));
                spannableString.setSpan(imageSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(spannableString);
            }else {
                textView.append(text.substring(start,index+6));
                start = index + 6;
            }
            index = text.indexOf("emoji_",index+1);
        }
        if (start > 0){
            textView.append(text.substring(start,text.length()));
        }
    }

    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(strNum).matches();
    }

}
