package com.here.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.User;
import com.here.personal.other.OtherInfoActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/17 00:20
 */

public class MyFollowAdapter extends BaseQuickAdapter<User> {

    public MyFollowAdapter(List<User> data) {
        super(R.layout.item_my_follow, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final User user) {
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
    }
}
