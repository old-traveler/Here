package com.here.chat;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by hyc on 2017/7/8 15:23
 */

public interface ChatContract {

    void loadingRecord(List<BmobIMMessage> bmobIMMessages);

    void showLoading();

    void stopLoading();

    int sendTextMessage(BmobIMMessage bmobIMMessage);

    void sendVoiceMessage();

    void sendImageMessage();

    void sendEmoji();

    void sendFail(String error);

    void sending(int value);

    void sendSuccess(int position);

    String getMessage();

    BmobIMConversation getConversation();

    void cleanInput();



}
