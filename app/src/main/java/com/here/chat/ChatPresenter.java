package com.here.chat;

import android.text.TextUtils;
import android.util.Log;

import com.here.base.BasePresenter;
import com.here.bean.CallMessage;
import com.here.bean.ImageMessage;
import com.here.bean.User;
import com.here.util.DbUtil;
import com.here.util.FileUtil;
import com.here.util.TinyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/7/8 15:22
 */

public class ChatPresenter extends BasePresenter<ChatContract>  {



    public void sendTextMessage(){
        BmobIMTextMessage msg =new BmobIMTextMessage();
        String message = mvpView.getMessage();
        msg.setContent(message);
        mvpView.cleanInput();
        Map<String,Object> map =new HashMap<>();
        msg.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
        final int position=mvpView.sendTextMessage(msg);
        mvpView.getConversation().sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                mvpView.sendSuccess(position);
            }
        });
    }

    public void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio =new BmobIMAudioMessage(local);
        audio.setDuration(length);
        audio.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
        final int position = mvpView.sendTextMessage(audio);
        mvpView.getConversation().sendMessage(audio, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                mvpView.sendSuccess(position);
            }
        });
    }

    public void sendImageMessage(String path){
        TinyUtil.compressChatImage(path, new TinyUtil.OnCompressListener() {
            @Override
            public void success(final String out) {
                String address = DbUtil.getInstance().queryImageCloudAddress(out);
                if (!TextUtils.isEmpty(address)){
                    ImageMessage image =new ImageMessage();
                    image.setExtraMap(FileUtil.getFileInfo(out));
                    image.setContent(address);
                    image.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
                    final int position = mvpView.sendTextMessage(image);
                    mvpView.getConversation().sendMessage(image, new MessageSendListener(){
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            mvpView.sendSuccess(position);
                            if (e!=null){
                                Log.i("错误",e.getMessage());
                            }
                        }
                    });
                }else {
                    BmobIMImageMessage image;
                    image = new BmobIMImageMessage(out);
                    image.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
                    final int position = mvpView.sendTextMessage(image);
                    mvpView.getConversation().sendMessage(image, new MessageSendListener(){
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            mvpView.sendSuccess(position);
                            if (e!=null){
                                Log.i("错误",e.getMessage());
                            }
                        }
                    });
                }

            }

            @Override
            public void fail() {
                mvpView.sendFail("图片压缩错误，请稍后再试");
            }
        });

    }

    public void sendVoiceRequest(BmobIMConversation conversation){
        mvpView.showLoading();
        CallMessage callMessage = new CallMessage();
        callMessage.setContent("[语音电话]");
        conversation.sendMessage(callMessage, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                mvpView.stopLoading();
                if(e == null){
                    mvpView.startVoiceChat();
                }else {
                    mvpView.sendFail("发送请求失败，请稍后再试");
                }
            }
        });
    }

    public void queryMessageRecord(BmobIMConversation conversation , BmobIMMessage message){
        conversation.queryMessages(message, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null){
                    mvpView.loadMessageRecord(list);
                }else {
                    mvpView.loadMessageRecord(new ArrayList<BmobIMMessage>());
                }
            }
        });
    }



}
