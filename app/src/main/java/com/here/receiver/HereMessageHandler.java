package com.here.receiver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.here.HereApplication;
import com.here.apply.ApplyActivity;
import com.here.apply.ApplyPresenter;
import com.here.bean.ImActivity;
import com.here.bean.User;
import com.here.going.GoingActivity;
import com.here.util.ImActivityUtil;
import com.here.util.JoinUtil;
import com.here.util.NotificationUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/8 13:30
 * 消息接收
 */

public class HereMessageHandler extends BmobIMMessageHandler {

    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        if (messageEvent.getMessage().getMsgType().equals("apply")) {
            Intent intent = new Intent(HereApplication.getContext(), ApplyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("apply", messageEvent);
            intent.putExtras(bundle);
            intent.putExtra("id",NotificationUtil.id+1);
            NotificationUtil.showNewNotification("活动申请", "用户" + messageEvent
                    .getFromUserInfo().getName() + "请求加入活动", true, intent);
        } else if (messageEvent.getMessage().getMsgType().equals("response")) {
            if (messageEvent.getMessage().getContent().indexOf("agree") != -1) {
                String objectId = messageEvent.getMessage().getContent().split("-")[1];
                ImActivityUtil.queryOneImActivity(objectId, new ImActivityUtil.OnGetOneImActivityListener() {
                    @Override
                    public void success(ImActivity imActivity) {
                        JoinUtil.cacheApplyUserId(BmobUser.getCurrentUser(User.class).getObjectId());
                        ImActivityUtil.cacheNewImActivity(imActivity);
                        JoinUtil.joinNewImActivity(false, imActivity.getOverTime());
                        Log.i("错误","跳转");
                        NotificationUtil.showNewNotification("活动申请", "您申请的活动，已经被同意，请及时联系伙伴",
                                true, new Intent(HereApplication.getContext(), GoingActivity.class));
                    }

                    @Override
                    public void fail(String error) {
                        Toast.makeText(HereApplication.getContext(), error, Toast.LENGTH_SHORT).show();
                        Log.i("错误",error);
                    }
                });
            } else {
                NotificationUtil.showNewNotification("活动申请", "您申请的活动，已经被拒绝，请选择其他活动",
                        false, null);
                JoinUtil.clearLimit();
            }
        } else {
            EventBus.getDefault().post(messageEvent.getMessage());
        }

    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        Map<String, List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            if (list.size() != 0) {
                for (MessageEvent messageEvent : list) {
                    if (messageEvent.getMessage().getMsgType().equals("apply")) {
                        Log.i("申请","收到一条离线申请");
                        Intent intent = new Intent(HereApplication.getContext(), ApplyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("apply", messageEvent);
                        intent.putExtras(bundle);
                        intent.putExtra("id",NotificationUtil.id+1);
                        NotificationUtil.showNewNotification("活动申请", "用户" + messageEvent
                                .getFromUserInfo().getName() + "请求加入活动", true, intent);
                    } else if (messageEvent.getMessage().getMsgType().equals("response")) {
                        if (messageEvent.getMessage().getContent().indexOf("agree") != -1) {
                            String objectId = messageEvent.getMessage().getContent().split("-")[1];
                            ImActivityUtil.queryOneImActivity(objectId, new ImActivityUtil.OnGetOneImActivityListener() {
                                @Override
                                public void success(ImActivity imActivity) {
                                    JoinUtil.cacheApplyUserId(BmobUser.getCurrentUser(User.class).getObjectId());
                                    ImActivityUtil.cacheNewImActivity(imActivity);
                                    JoinUtil.joinNewImActivity(false, imActivity.getOverTime());
                                    NotificationUtil.showNewNotification("活动申请", "您申请的活动，已经被同意，请及时联系伙伴",
                                            true, new Intent(HereApplication.getContext(), GoingActivity.class));
                                }

                                @Override
                                public void fail(String error) {

                                }
                            });
                        } else {
                            NotificationUtil.showNewNotification("活动申请", "您申请的活动，已经被拒绝，请选择其他活动",
                                    false, null);
                            JoinUtil.clearLimit();
                        }
                    } else {
                        EventBus.getDefault().post(messageEvent.getMessage());
                    }
                }
            }
        }
    }


}
