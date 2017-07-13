package com.here.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.here.R;
import com.here.base.MvpFragment;

/**
 * Created by hyc on 2017/6/21 14:59
 */

public class CommunityFragment extends MvpFragment<CommunityPresenter> implements CommunityContract {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvpPresenter=createPresenter();
        View view = inflater.inflate(R.layout.fragment_community,container,false);
        mvpPresenter.attachView(this);
        return view;
    }

    @Override
    protected CommunityPresenter createPresenter() {
        return new CommunityPresenter();
    }
}
