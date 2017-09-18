package com.here.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.ImActivity;
import com.here.imdetails.ImDetailsActivity;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/17 19:16
 */

public class RecordAdapter extends BaseQuickAdapter<ImActivity> {

    public RecordAdapter(List<ImActivity> data) {
        super(R.layout.item_my_activity, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ImActivity imActivity) {
        baseViewHolder.setText(R.id.tv_item_activity_time,imActivity.getPublishDate());
        baseViewHolder.setText(R.id.tv_item_activity_title,imActivity.getTitle());
        baseViewHolder.setText(R.id.tv_item_activity_content,imActivity.getDescribe());
        if (imActivity.getPublisher() != null){
            baseViewHolder.setText(R.id.tv_item_activity_name,imActivity.getPublisher().getNickname());
            Glide.with(mContext)
                    .load(imActivity.getPublisher().getHeadImageUrl())
                    .into((CircleImageView) baseViewHolder.getView(R.id.cv_item_activity_head));
        }
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imActivity",imActivity);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


    }
}
