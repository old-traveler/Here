package com.here.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.bean.Kind;
import com.here.util.DensityUtil;
import java.util.List;

/**
 * Created by hyc on 2017/9/7 20:53
 */

public class NearAdapter extends BaseQuickAdapter<Kind> {

    public NearAdapter(List<Kind> data) {
        super(R.layout.item_near, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Kind s) {
        Glide.with(mContext).load(s.getImgId())
                .into((ImageView) baseViewHolder.getView(R.id.iv_item_near));
        LinearLayout linearLayout = baseViewHolder.getView(R.id.ll_item_near);
        if ((baseViewHolder.getAdapterPosition()+1) % 2 == 0){
            DensityUtil.setViewMargin(linearLayout, false, 3, 0, 0, 6);
        } else {
            DensityUtil.setViewMargin(linearLayout, false, 0, 3, 0, 6);
        }
        baseViewHolder.setText(R.id.tv_kind_name,s.getName());
    }

}
