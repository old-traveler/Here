package com.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.here.R;
import com.here.bean.FindImage;
import com.here.personal.PersonalActivity;
import com.here.personal.other.OtherInfoActivity;
import com.here.util.DbUtil;
import com.here.util.DensityUtil;
import java.util.List;
import java.util.Random;
import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/9/23 15:21
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.FindViewHolder> {

    private int width;

    private List<FindImage> findImages;

    private Context mContext;

    public FindAdapter(List<FindImage> findImages){
        this.findImages = findImages;
    }

    @Override
    public FindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new FindViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.item_find_image,parent,false));
    }

    @Override
    public void onBindViewHolder(FindViewHolder holder, int position) {
        holder.load(position);
    }

    @Override
    public int getItemCount() {
        return findImages == null ? 0:findImages.size();
    }

    public List<FindImage> getFindImages() {
        return findImages;
    }

    public void ignoreFind(int position){
        DbUtil.getInstance().addIgnoreRecord(BmobUser.getCurrentUser()
                .getObjectId(), findImages.get(position).getObjectId());
        findImages.remove(position);
    }

    public void setFindImages(List<FindImage> findImages) {
        this.findImages = findImages;
        notifyDataSetChanged();
    }

    public void addFindImage(List<FindImage> images){
        for (FindImage image : images) {
            findImages.add(image);
            notifyItemInserted(findImages.size()-1);
        }
    }

    public void setWidth(int width) {
        this.width = width/2;
    }


    class FindViewHolder extends RecyclerView.ViewHolder{

        private ImageView paletteImageView;

        private View itemView;

        private CardView cvFind;

        public FindViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            paletteImageView = (ImageView) itemView.findViewById(R.id.palette);
            cvFind = (CardView) itemView.findViewById(R.id.cv_find);
        }

        public void load(final int position){
            final int imageWidth = width - DensityUtil.dip2px(14);
            final int imageHeight = imageWidth*findImages.get(position)
                    .getHeight()/findImages.get(position).getWidth();
            StaggeredGridLayoutManager.LayoutParams layoutParams = (
                    StaggeredGridLayoutManager.LayoutParams)itemView.getLayoutParams();
            layoutParams.height = imageHeight + 10;
            itemView.setLayoutParams(layoutParams);

            if ((position+1) % 2 == 0){
                DensityUtil.setViewMargin(cvFind,true,7,7,0 ,10);
            }else{
                DensityUtil.setViewMargin(cvFind,true,7,7,0 ,10);
            }
            int color = 0;
            switch (new Random().nextInt(10)){
                case 0:color = Color.parseColor("#FF9966");break;
                case 1:color = Color.parseColor("#99CCCC");break;
                case 2:color = Color.parseColor("#ff6a88");break;
                case 3:color = Color.parseColor("#ff92ff");break;
                case 4:color = Color.parseColor("#70e6af");break;
                case 5:color = Color.parseColor("#87CEFA");break;
                case 6:color = Color.parseColor("#ecb6b6");break;
                case 7:color = Color.parseColor("#ff8843");break;
                case 8:color = Color.parseColor("#f95f6e");break;
                case 9:color = Color.parseColor("#a8ae52");break;
            }
            cvFind.setCardBackgroundColor(color);
            cvFind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findImages.get(position).getMaster().getObjectId()
                            .equals(BmobUser.getCurrentUser().getObjectId())){
                        Intent intent = new Intent(mContext, PersonalActivity.class);
                        mContext.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContext, OtherInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("other",findImages.get(position).getMaster());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                }
            });
            Glide.with(mContext).load(findImages.get(position).getUrl())
                    .override(imageWidth ,imageHeight).priority(Priority.IMMEDIATE)
                    .into(paletteImageView);
        }
    }
}
