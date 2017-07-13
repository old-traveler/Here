package com.here.base;

import android.os.Bundle;
import android.view.View;

/**
 * Created by hyc on 2017/6/21 13:57
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment{
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }



    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
