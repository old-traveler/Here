package com.here.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by hyc on 2017/7/5 16:39
 */

public class MyGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = false;

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }


    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
