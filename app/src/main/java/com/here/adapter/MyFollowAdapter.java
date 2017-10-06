package com.here.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.User;
import com.here.personal.other.OtherInfoActivity;
import com.here.util.AccountUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/17 00:20
 */

public class MyFollowAdapter extends BaseQuickAdapter<User> {

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    private OnItemClickListener listener;

    public MyFollowAdapter(List<User> data) {
        super(R.layout.item_my_follow, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final User user) {
        baseViewHolder.setText(R.id.tv_follow_nickname,user.getNickname());
        baseViewHolder.setText(R.id.tv_follow_info,user.getIntroduction());
        Glide.with(mContext)
                .load(user.getHeadImageUrl())
                .into((CircleImageView) baseViewHolder.getView(R.id.cv_item_follow));
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("other",user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null){
                    new AlertView("温馨提示", "是否将该用户移除黑名单", "确定", new String[]{"取消"}, null, mContext,
                            AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == -1) {
                                listener.onClick(baseViewHolder.getAdapterPosition());
                            }
                        }
                    }).show();
                    return true;
                }
                return false;
            }
        });
    }
}
