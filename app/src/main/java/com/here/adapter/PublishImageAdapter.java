package com.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.HereApplication;
import com.here.R;
import com.here.photo.PhotoActivity;
import com.here.photo.PhotoPresenter;

import java.util.List;

/**
 * Created by hyc on 2017/7/4 09:21
 */

public class PublishImageAdapter extends BaseQuickAdapter<String> {



    public interface OnItemClickListener{
        void onClick();
    }
    private OnItemClickListener listener;

    public PublishImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final String s) {
        if (!s.equals("add image")){
            baseViewHolder.getView(R.id.iv_item_delete).setVisibility(View.VISIBLE);
            Glide.with(HereApplication.getContext())
                    .load(s)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_item_publish));

            baseViewHolder.setOnClickListener(R.id.iv_item_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getData().indexOf(s);
                    getData().remove(s);
                    notifyItemRemoved(position);
                }
            });

            baseViewHolder.setOnClickListener(R.id.iv_item_publish, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoPresenter.imageUrl=s;
                    mContext.startActivity(new Intent(mContext,PhotoActivity.class));
                }
            });

        }else if (getData().indexOf(s)==getItemCount()-1){
            baseViewHolder.getView(R.id.iv_item_delete).setVisibility(View.GONE);
            Glide.with(HereApplication.getContext())
                    .load(R.drawable.add_image)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_item_publish));
            baseViewHolder.setOnClickListener(R.id.iv_item_publish, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null&&s.equals("add image")){
                        listener.onClick();
                    }
                }
            });
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
