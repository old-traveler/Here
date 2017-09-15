package com.here.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.R;
import com.here.photo.PhotoActivity;
import com.here.photo.PhotoPresenter;
import com.here.util.CommonUtils;

import java.util.List;

/**
 * Created by hyc on 2017/7/8 10:44
 */

public class DetailsImageAdapter extends BaseQuickAdapter<String>  {

    public DetailsImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final String s) {
        if (getItemCount() == 1){
            Glide.with(mContext)
                    .load(s)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int[] size = CommonUtils.zoomImageDetail(resource
                                    .getWidth(),resource.getHeight());
                            Glide.with(mContext)
                                    .load(s)
                                    .override(size[0],size[1])
                                    .into((ImageView) baseViewHolder.getView(R.id.item_details_image));
                        }
                    });
        }else {
            Glide.with(mContext)
                    .load(s)
                    .into((ImageView) baseViewHolder.getView(R.id.item_details_image));
        }

        baseViewHolder.setOnClickListener(R.id.item_details_image, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPresenter.imageUrl = s;
                Intent intent = new Intent(mContext, PhotoActivity.class);
                mContext.startActivity(intent);
            }
        });

    }
}
