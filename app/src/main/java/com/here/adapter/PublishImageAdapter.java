package com.here.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.here.HereApplication;
import com.here.R;
import com.here.photo.PhotoActivity;
import com.here.photo.PhotoPresenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 2017/7/4 09:21
 */

public class PublishImageAdapter extends BaseQuickAdapter<String> {

    private WeakReference<Activity> activity;

    public void setActivity(WeakReference<Activity> activity) {
        this.activity = activity;
    }

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
                    ArrayList<String> image = new ArrayList<>();
                    for (String s1 : getData()) {
                        image.add(s1);
                    }
                    Pair<View, String> p = new Pair<View, String>(v, "image");
                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    intent.putStringArrayListExtra("images", image);
                    intent.putExtra("position",baseViewHolder.getAdapterPosition());
                    activity.get().startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(activity.get(), p).toBundle());
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
