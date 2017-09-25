package com.here.view;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.here.adapter.FindAdapter;
import com.here.util.CommonUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by hyc on 2017/9/25 09:49
 */

public class FindCallBack extends ItemTouchHelper.Callback {

    private int dragFlags;
    private int swipeFlags;
    private FindAdapter adapter;
    private boolean isUp;


    public FindCallBack(FindAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        }
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        adapter.notifyDataSetChanged();
        initState();
    }

    private void initState() {
        if (null != dragListener){
            dragListener.deleteState(false);
            dragListener.dragState(false);
        }

        isUp = false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
            , float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (null == dragListener){
            return;
        }
        Log.i("测试",dY+"  "+recyclerView.getHeight()+"  "+viewHolder.itemView.getBottom());
        if (dY >= recyclerView.getHeight()
                - viewHolder.itemView.getBottom()
                - CommonUtils.dipToPx(50)){
            dragListener.deleteState(true);
            if (isUp){
                viewHolder.itemView.setVisibility(View.INVISIBLE);
                adapter.ignoreFind(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                initState();
                return;
            }
        }else {
            if (View.INVISIBLE == viewHolder.itemView.getVisibility()){
                dragListener.dragState(false);
            }
            dragListener.deleteState(false);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && dragListener != null){
            dragListener.dragState(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        isUp = true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    public interface DragListener {
        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         *
         * @param delete
         */
        void deleteState(boolean delete);

        /**
         * 是否于拖拽状态
         *
         * @param start
         */
        void dragState(boolean start);
    }

    private DragListener dragListener;

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }



}
