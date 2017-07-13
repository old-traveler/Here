package com.here.base;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hyc on 2017/6/21 13:43
 */

public class BasePresenter<V> {
    public V mvpView;

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }


    public void detachView() {
        this.mvpView = null;
    }
}
