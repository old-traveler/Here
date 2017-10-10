package com.here.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;

import java.util.List;

/**
 * Created by hyc on 2017/10/8 12:51
 */

public class EmotionAdapter extends BaseQuickAdapter<Integer> {

    private OnEmotionListener listener;

    public EmotionAdapter setListener(OnEmotionListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnEmotionListener{
        void onClick(int position);
    }

    public EmotionAdapter( List<Integer> data) {
        super(R.layout.item_emotion,data);
    }



    @Override
    protected void convert(final BaseViewHolder baseViewHolder, Integer integer) {
        Glide.with(mContext).load(integer).dontAnimate()
                .into((ImageView) baseViewHolder.getView(R.id.iv_item_emoji));
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(baseViewHolder.getAdapterPosition()+1);
            }
        });
    }
}
