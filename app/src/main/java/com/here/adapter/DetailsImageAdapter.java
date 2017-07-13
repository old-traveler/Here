package com.here.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;

import java.util.List;

/**
 * Created by hyc on 2017/7/8 10:44
 */

public class DetailsImageAdapter extends BaseQuickAdapter<String>  {

    public DetailsImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        Glide.with(mContext)
                .load(s)
                .into((ImageView) baseViewHolder.getView(R.id.item_details_image));
    }
}
