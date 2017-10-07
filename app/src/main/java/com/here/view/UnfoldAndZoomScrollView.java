package com.here.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.here.util.CommonUtils;

/**
 * 仿QQ个人信息详情界面中的背景图下拉扩展放大 Created by hyc on 2017/5/3 14:40
 */

public class UnfoldAndZoomScrollView extends NestedScrollView  implements Animator.AnimatorListener,NestedScrollView.OnScrollChangeListener {

    public UnfoldAndZoomScrollView(Context context) {
        super(context);
    }

    public UnfoldAndZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnfoldAndZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 记录下拉View时的起始位置
     */
    private float pullY = 0f;
    /**
     * 记录扩展放大View的原始宽度
     */
    private int viewWidth = 0;
    /**
     * 记录扩展放大View的原始高度
     */
    private int viewHeight = 0;
    /**
     * View是否下拉扩展
     */
    private boolean isUnfolding = false;
    /**
     * View 是否被下拉放大
     */
    private boolean isZoom = false;
    /**
     * 放大系数
     */
    private float zoomRatio = 0.4f;
    /**
     * 最大放大比例
     */
    private float maxZoomRatio = 2f;
    /**
     * 回弹时间系数
     */
    private float replyTimeRatio = 0.5f;
    /**
     * 头部视图隐藏高度
     */
    private int hideHeight = 0;
    /**
     * 扩展放大的头部视图
     */
    private View headView;
    private View figure;
    private View content;

    /**
     * 隐藏头部视图高度的比例   为4时即隐藏头部视图顶部1/4高度与底部1/4高度
     */
    private int hideRatio = 5;

    private float damper=0.0f;

    private UpSlipListener upSlipListener;

    public void setUpSlipListener(UpSlipListener upSlipListener) {
        this.upSlipListener = upSlipListener;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY>=0&&scrollY<viewHeight*2.0f){
            narrowHeadPortrait(scrollY);
            setToolBarBg(scrollY);

        }
    }


    public interface UpSlipListener{
        void upSlipping(float distance);
    }

    public void setHideRatio(int hideRatio) {
        this.hideRatio = hideRatio;
    }

    public void setZoomRatio(float zoomRatio) {
        this.zoomRatio = zoomRatio;
    }

    public void setMaxZoomRatio(float maxZoomRatio) {
        this.maxZoomRatio = maxZoomRatio;
    }

    public void setReplyTimeRatio(float replyTimeRatio) {
        this.replyTimeRatio = replyTimeRatio;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取头部视图的原始宽高
        if (viewWidth <= 0 || viewHeight <=0) {
            viewWidth = headView.getMeasuredWidth();
            viewHeight = headView.getMeasuredHeight();
            figureWidth = CommonUtils.dipToPx(100);
            figureHeight = CommonUtils.dipToPx(100);
        }
        //绘制视图时隐藏头部View的顶部和底部
        if (hideHeight==0){
            setOnScrollChangeListener(UnfoldAndZoomScrollView.this);
            hideHeight=viewHeight/hideRatio;
            ViewGroup.LayoutParams layoutParams = headView.getLayoutParams();
            ((MarginLayoutParams) layoutParams).setMargins(0, -hideHeight, 0,-hideHeight);
            headView.setLayoutParams(layoutParams);
            ViewGroup.LayoutParams layoutParams2 = content.getLayoutParams();
            ((MarginLayoutParams) layoutParams2).setMargins(0, viewHeight-2*hideHeight, 0,0);
            content.setLayoutParams(layoutParams2);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        不可过度滚动，否则上移后下拉会出现部分空白的情况
        setOverScrollMode(OVER_SCROLL_NEVER);
//        获得默认第一个view
        if (getChildAt(0) != null && getChildAt(0) instanceof ViewGroup && headView == null) {
            ViewGroup mViewGroup = (ViewGroup) getChildAt(0);
            if (mViewGroup.getChildCount() > 0) {
                headView = mViewGroup.getChildAt(0);
                figure=mViewGroup.getChildAt(2);
                content=mViewGroup.getChildAt(1);
                content.setBackgroundColor(Color.WHITE);
            }
        }
    }




    /**
     * 下拉扩展的高度
     */
    private float unfoldHeight = 0f;



    private boolean isNarrow=false;
    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        if (viewWidth <= 0 || viewHeight <=0) {
            viewWidth = headView.getMeasuredWidth();
            viewHeight = headView.getMeasuredHeight();
        }
        if (headView == null || viewWidth <= 0 || viewHeight <= 0) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isAnimator){
                    return false;
                }
                if (!isNarrow&&getScrollY()>0){
                    isNarrow=true;
                    isUnfolding=false;
                    break;

                }else if (getScrollY()==0){
                    isNarrow=false;
                    narrowHeadPortrait(0);
                    setToolBarBg(0);
                }
                if (isNarrow){
                    break;
                }


                if (!isUnfolding&&!isNarrow) {
                    if (getScrollY() == 0) {
                        pullY = ev.getY();//滑动到顶部时，记录位置
                    }else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - pullY)*zoomRatio);
                damper=1.0f;
                distance=(int)(distance*damper);
                if (distance < 0) {
                    isNarrow=true;
                    isUnfolding=false;
                    break;//若往下滑动
                }
                isUnfolding = true;
                if (hideHeight>distance){
                    unfoldImage(distance);
                    unfoldHeight=distance;
                    return true;
                }

                setZoom(distance-hideHeight);

                return true;
            case MotionEvent.ACTION_UP:
                isUnfolding = false;
                if (!isUnfolding&&!isNarrow){
                    replyView();
                }



                break;
        }
        return super.onTouchEvent(ev);
    }





    /**
     * 扩展头部视图
     * @param distance 顶部和底部扩展的距离
     */
    private void unfoldImage(float distance) {
        if (distance==0){
            return;
        }
        ViewGroup.LayoutParams layoutParams = headView.getLayoutParams();
        ((MarginLayoutParams) layoutParams).setMargins(0,
                (int)(distance-hideHeight),0,(int)(distance-hideHeight));
        headView.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams1 = figure.getLayoutParams();
        ((MarginLayoutParams) layoutParams1).setMargins(0, (int) (viewHeight-2*hideHeight-figureWidth/2+distance),0,0);
        figure.setLayoutParams(layoutParams1);

        ViewGroup.LayoutParams layoutParams2 = content.getLayoutParams();
        ((MarginLayoutParams) layoutParams2).setMargins(0, (int) (viewHeight-2*hideHeight+distance),0,0);
        content.setLayoutParams(layoutParams2);



    }



    int figureWidth;
    int figureHeight;


    private void narrowHeadPortrait(int distance){
        distance= (int) (distance*0.5f);
        if (distance>figureWidth/2||distance<0){
            return;
        }
        ViewGroup.LayoutParams layoutParams = figure.getLayoutParams();
        layoutParams.width = figureWidth-2*distance;
        layoutParams.height = figureHeight-2*distance;
        ((MarginLayoutParams) layoutParams).setMargins(0, viewHeight-2*hideHeight-figureWidth/2+distance, 0, 0);
        figure.setLayoutParams(layoutParams);

    }

    public void setToolBarBg(int distance){
        if (3*distance>viewHeight){
            distance=viewHeight/3;
        }
        if (upSlipListener!=null){
            upSlipListener.upSlipping(3.0f*distance/(1.0f*viewHeight));
        }
    }

    /**
     * 放大头部View
     * @param distance  放大的距离
     */

    private float ZoomDistance;
    private void setZoom(float distance) {
        float scaleTimes = (float) ((viewWidth+distance)/(viewWidth*1.0));
//        如超过最大放大倍数，直接返回
        if (scaleTimes > maxZoomRatio) return;

        ViewGroup.LayoutParams layoutParams = headView.getLayoutParams();
        layoutParams.width = (int) (viewWidth + distance);
        layoutParams.height = (int)(viewHeight*((viewWidth+distance)/viewWidth));
//        设置控件水平居中
        ((MarginLayoutParams) layoutParams).setMargins(0, 0, 0, 0);
        headView.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams layoutParams1 = figure.getLayoutParams();
        ((MarginLayoutParams) layoutParams1).setMargins(0, (int) (viewHeight-hideHeight-figureWidth/2+distance),0,0);
        figure.setLayoutParams(layoutParams1);
        ViewGroup.LayoutParams layoutParams2 = content.getLayoutParams();
        ((MarginLayoutParams) layoutParams2).setMargins(0, (int) (viewHeight-hideHeight+distance),0,0);
        content.setLayoutParams(layoutParams2);
        isZoom=true;
        if (!isReplying){
            ZoomDistance=distance;
        }

    }


    private boolean isReplying=false;
    private boolean replay=false;
    private boolean isAnimator=false;

    /**
     * 头部视图还原
     */
    private void replyView() {
        /**
         * 如果头部视图被放大，添加动画还原放大的头部视图
         */
        isAnimator=true;
        if (isZoom){
            isReplying=true;
            // 设置动画

            ValueAnimator anim = ObjectAnimator.ofFloat(ZoomDistance, 0.0F)
                    .setDuration((long) ZoomDistance);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setZoom((Float) animation.getAnimatedValue());
                    if (!replay&&(Float) animation.getAnimatedValue()<10.0f){
                        ValueAnimator unfold = ObjectAnimator.ofFloat(hideHeight
                                , 0.0F).setDuration((long)hideHeight);
                        unfold.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                unfoldImage((Float) animation.getAnimatedValue());
                            }
                        });
                        unfold.start();
                        unfold.addListener(UnfoldAndZoomScrollView.this);
                        replay=true;
                    }
                }
            });
            anim.start();
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isReplying=false;
                    isZoom=false;
                    ZoomDistance=0;
                    replay=false;
                    unfoldHeight=0;

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }else {
            /**
             * 将扩展出来的头部视图还原
             */
            ValueAnimator unfold = ObjectAnimator.ofFloat(unfoldHeight, 0.0F).setDuration((long) (unfoldHeight));
            unfold.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    unfoldImage((Float) animation.getAnimatedValue());
                }
            });
            unfold.start();
            unfold.addListener(this);
            unfoldHeight=0;
        }

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener!=null) onScrollListener.onScroll(l,t,oldl,oldt);
    }

    private UnfoldAndZoomScrollView.OnScrollListener onScrollListener;
    public void setOnScrollListener(UnfoldAndZoomScrollView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }



    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isAnimator=false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * 滑动监听
     */
    public  interface OnScrollListener{
        void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }


}
