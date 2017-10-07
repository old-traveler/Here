package com.here.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.User;
import com.here.personal.PersonalActivity;
import com.here.personal.other.OtherInfoActivity;
import com.here.util.AccountUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/17 00:20
 */

public class MyFollowAdapter extends BaseQuickAdapter<User> {

    private WeakReference<Activity> context;
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setContext(WeakReference<Activity> context) {
        this.context = context;
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
                if (user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())){
                    Intent intent = new Intent(context.get(),PersonalActivity.class);
                    context.get().startActivity(intent);
                }else {
                    Pair<View, String> p = new Pair<View, String>(baseViewHolder.getView(R.id.cv_item_follow), "image");
                    Intent intent = new Intent(mContext, OtherInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("other",user);
                    intent.putExtras(bundle);
                    context.get().startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(context.get(), p).toBundle());
                }

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
