package com.here.view;

import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.UP;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        ImageAdapter.ImageViewHolder holder = (ImageAdapter
                .ImageViewHolder) viewHolder;
        holder.isShowTips(false);
    }

    @Override
    public void onSelectedChanged(final RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSendImage(null);
            }
        }, 500);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        isUp = false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView
            .ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            ImageAdapter.ImageViewHolder holder = (ImageAdapter
                    .ImageViewHolder) viewHolder;
            if (dY < -DensityUtil.dip2px(70)){
                if (isUp){
                    holder.isShowTips(false);
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
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
        void onSendImage(View view);
    }


    private void sendImageAnimation(View view,int rightDistance
            ,int upDistance,float zoomScale){
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
        view.startAnimation(as);

    }
}
