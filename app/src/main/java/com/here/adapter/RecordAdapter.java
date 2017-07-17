package com.here.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.ImActivity;

import java.util.List;

/**
 * Created by hyc on 2017/7/17 19:16
 */

public class RecordAdapter extends BaseQuickAdapter<ImActivity> {

    public RecordAdapter(List<ImActivity> data) {
        super(R.layout.item_my_activity, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ImActivity imActivity) {
        baseViewHolder.setText(R.id.tv_item_activity_time,imActivity.getPublishDate());
        baseViewHolder.setText(R.id.tv_item_activity_name,imActivity.getPublisher().getName());
        baseViewHolder.setText(R.id.tv_item_activity_title,imActivity.getTitle());
        baseViewHolder.setText(R.id.tv_item_activity_content,imActivity.getDescribe());
        Glide.with(mContext)
                .load(imActivity.getPublisher().getHeadImageUrl())
                .into((ImageView) baseViewHolder.getView(R.id.cv_item_activity_head));
    }
}
