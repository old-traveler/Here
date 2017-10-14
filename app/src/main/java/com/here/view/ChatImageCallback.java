package com.here.view;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import com.here.adapter.ImageAdapter;
import com.here.util.DensityUtil;

/**
 * Created by hyc on 2017/10/10 15:39
 */

public class ChatImageCallback extends ItemTouchHelper.Callback {
    private RecyclerView recyclerView;

    private boolean isUp;
    private OnSwipeImageListener listener;
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        this.recyclerView = recyclerView;
        int dragFlags = ItemTouchHelper.UP;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags,swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(final RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        isUp = false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView
            .ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            ImageAdapter.ImageViewHolder holder = (ImageAdapter
                    .ImageViewHolder) viewHolder;
            if (dY < -DensityUtil.dip2px(100)){
                if (isUp){
                    listener.onSendImage(holder.getUrl());
                    holder.isShowTips(false);
                    sendImageAnimation(viewHolder);
                    isUp = false;
                }else {
                    holder.isShowTips(true);
                }
            }else {
                holder.isShowTips(false);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        isUp = true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    public void setListener(OnSwipeImageListener listener) {
        this.listener = listener;
    }

    public interface OnSwipeImageListener{
        void onSendImage(String url);
    }


    private void sendImageAnimation(RecyclerView.ViewHolder view){
        int rightDistance=0;
        int upDistance=0;
        float zoomScale=0;
//        CommonUtils.zoomImage()
        AnimationSet as = new AnimationSet(true);
        as.setDuration(500);
        TranslateAnimation ta = new TranslateAnimation(0
                , rightDistance, 0, upDistance);
        ta.setDuration(500);
        as.addAnimation(ta);
        ScaleAnimation sa = new ScaleAnimation(1
                , zoomScale, 1, zoomScale,
                Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        sa.setDuration(500);
        as.addAnimation(sa);
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(500);
        as.addAnimation(aa);
        view.itemView.startAnimation(as);

    }
}
