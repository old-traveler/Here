package com.here.follow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.here.R;
import com.here.base.MvpFragment;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public class FollowFragment extends MvpFragment<FollowPresenter> implements FollowContract {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvpPresenter=createPresenter();
        View view = inflater.inflate(R.layout.fragment_follow,container,false);

        mvpPresenter.attachView(this);
        return view;
    }

    @Override
    protected FollowPresenter createPresenter() {
        return new FollowPresenter();
    }
}
