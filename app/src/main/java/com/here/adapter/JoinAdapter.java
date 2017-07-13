package com.here.adapter;


import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.Join;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/7/5 20:34
 */

public class JoinAdapter extends BaseQuickAdapter<Join> {

    public JoinAdapter(List<Join> data) {
        super(R.layout.item_join_user, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Join user) {
        if (getData().indexOf(user) == getData().size()-1){
            Glide.with(mContext)
                    .load(R.drawable.invitation)
                    .into((CircleImageView) baseViewHolder.getView(R.id.cv_join_user));
        }else {

            if (!TextUtils.isEmpty(user.getJoinUser().getHeadImageUrl())){
                Glide.with(mContext)
                        .load(user.getJoinUser().getHeadImageUrl())
                        .into((CircleImageView) baseViewHolder.getView(R.id.cv_join_user));
            }else {
                Glide.with(mContext)
                        .load(R.drawable.text_head)
                        .into((CircleImageView) baseViewHolder.getView(R.id.cv_join_user));
            }
        }


    }
}
