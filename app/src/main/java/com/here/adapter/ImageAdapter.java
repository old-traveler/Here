package com.here.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.util.DensityUtil;
import com.here.util.FileUtil;
import com.imnjh.imagepicker.adapter.BaseRecycleCursorAdapter;
import com.imnjh.imagepicker.model.Photo;


/**
 * Created by hyc on 2017/10/8 20:54
 */

public class ImageAdapter extends BaseRecycleCursorAdapter<RecyclerView.ViewHolder>{


    public ImageAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
        imageViewHolder.load(cursor);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_image,parent,false));
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView sendTips;
        private String url;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_chat_image);
            sendTips = (TextView) itemView.findViewById(R.id.tv_send_tips);
        }

        public void load(Cursor cursor){
            final Photo photo = Photo.fromCursor(cursor);
            url = photo.getFilePath();
            int[] size = FileUtil.getImageSize(photo.getFilePath());
            DensityUtil.setViewSize(imageView,DensityUtil.dip2px(150)
                    *size[0]/size[1],DensityUtil.dip2px(150));
            Glide.with(mContext).load(photo.getFilePath()).into(imageView);
        }

        public void isShowTips(boolean isShow){
            sendTips.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }


        public String getUrl() {
            return url;
        }
    }

}
