package com.here.adapter;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/16 15:52
 */

public class CommentAdapter extends BaseQuickAdapter<Comment>{

    public CommentAdapter(List<Comment> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Comment comment) {
        baseViewHolder.setText(R.id.tv_comment_nickname,comment.getUser().getNickname());
        baseViewHolder.setText(R.id.tv_comment_content,comment.getContent());
        baseViewHolder.setText(R.id.tv_comment_time,""+comment.getCreatedAt());
        Glide.with(mContext)
                .load(comment.getUser().getHeadImageUrl())
                .into((CircleImageView) baseViewHolder.getView(R.id.cv_comment_head));
    }
}
