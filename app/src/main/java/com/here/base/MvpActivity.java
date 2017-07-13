package com.here.base;

import android.os.Bundle;

/**
 * Created by hyc on 2017/6/21 13:57
 */

public abstract class MvpActivity <P extends BasePresenter>extends BaseActivity  {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
            mvpPresenter =null;
        }
    }
}