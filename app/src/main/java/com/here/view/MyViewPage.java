package com.here.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hyc on 2017/6/21 14:28
 */

public class MyViewPage extends ViewPager {

    private int rightDistance = 950;


    public MyViewPage(Context context) {
        this(context,null);
    }

    public MyViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getAction()==MotionEvent.ACTION_DOWN&&ev.getX()<rightDistance&&getCurrentItem() == 0 ){
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN&&ev.getX()<rightDistance&&getCurrentItem() == 0 ){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setRightDistance(int distance){
        this.rightDistance = distance*19/20;
    }

}
