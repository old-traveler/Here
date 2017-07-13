package com.here.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.HereApplication;
import com.here.R;
import com.here.chat.ChatActivity;
import com.here.login.LoginActivity;
import com.here.setting.SettingActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by hyc on 2017/7/8 15:33
 */

public class MessageAdapter extends BaseQuickAdapter<BmobIMConversation>{

    public MessageAdapter(List<BmobIMConversation> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final BmobIMConversation bmobIMConversation) {
        baseViewHolder.setText(R.id.tv_message_nickname,bmobIMConversation.getConversationTitle());
        SimpleDateFormat  time = new SimpleDateFormat("MM-dd-hh:mm");
        if (bmobIMConversation.getMessages().size()-1 > -1){
            String message_time = time.format(new Date(bmobIMConversation.getMessages()
                    .get(bmobIMConversation.getMessages().size()-1).getUpdateTime()));
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (System.currentTimeMillis() - bmobIMConversation.getMessages().get(bmobIMConversation.getMessages()
                    .size()-1).getUpdateTime()<1000*60*60*24L && Integer.parseInt(message_time.split("-")[1]) == day){
                baseViewHolder.setText(R.id.tv_message_time,message_time.split("-")[2]);
            }else {
                baseViewHolder.setText(R.id.tv_message_time,message_time.substring(0,5));
            }
            baseViewHolder.setText(R.id.tv_message_content,bmobIMConversation.getMessages().get(bmobIMConversation.getMessages().size()-1).getContent());
        }
        if (bmobIMConversation.getUnreadCount()>0){
            baseViewHolder.getView(R.id.rl_is_notice).setVisibility(View.VISIBLE);
        }else {
            baseViewHolder.getView(R.id.rl_is_notice).setVisibility(View.GONE);
        }

        Glide.with(HereApplication.getContext())
                .load(bmobIMConversation.getConversationIcon())
                .into((CircleImageView) baseViewHolder.getView(R.id.cv_message_head));
        baseViewHolder.setOnClickListener(R.id.rl_message_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobIMUserInfo info = new BmobIMUserInfo();
                info.setAvatar(bmobIMConversation.getConversationIcon());
                info.setUserId(bmobIMConversation.getConversationId());
                info.setName(bmobIMConversation.getConversationTitle());
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation c, BmobException e) {
                        if(e==null){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("c", c);
                            startActivity(ChatActivity.class, bundle);
                        }else{
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        baseViewHolder.setOnLongClickListener(R.id.rl_message_item, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertView("温馨提示", "是否删除该会话", "确定", new String[]{"取消"}, null, mContext,
                        AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == -1) {
                            bmobIMConversation.delete();
                            int item=getData().indexOf(bmobIMConversation);
                            getData().remove(item);
                            notifyItemRemoved(item);
                        }
                    }
                }).show();
                return true;
            }
        });

    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, target);
        if (bundle != null)
            intent.putExtra(mContext.getPackageName(), bundle);
        mContext.startActivity(intent);

    }
}
