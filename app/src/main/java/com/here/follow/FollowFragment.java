package com.here.follow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.adapter.CommunityDetailsAdapter;
import com.here.base.MvpFragment;
import com.here.bean.Community;
import com.here.publish.appointment.AppointmentActivity;
import com.here.publish.share.ShareActivity;
import com.here.util.CommunityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public class FollowFragment extends MvpFragment<FollowPresenter> implements FollowContract {


    @Bind(R.id.rv_follow)
    RecyclerView rvFollow;
    @Bind(R.id.fb_add_activity)
    FloatingActionButton fbAddActivity;
    @Bind(R.id.sl_follow)
    SmartRefreshLayout slFollow;
    private CommunityDetailsAdapter adapter;
    private boolean isLoad = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        ButterKnife.bind(this, view);
        mvpPresenter.attachView(this);
        if (isLoad){
            adapter = new CommunityDetailsAdapter(new ArrayList<Community>(), getContext());
            initView();
            slFollow.autoRefresh();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !isLoad) {
            isLoad = true;
        }else {
            isLoad = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {
        slFollow.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mvpPresenter.queryAppointment(true, refreshlayout);
            }
        });

        slFollow.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });

        adapter = new CommunityDetailsAdapter(new ArrayList<Community>(), getActivity());
        rvFollow.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFollow.setAdapter(adapter);

    }

    @Override
    protected FollowPresenter createPresenter() {
        return new FollowPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fb_add_activity)
    public void onViewClicked() {
        new AlertView("发布", null, "取消", new String[]{"发布心情"}, new String[]{"发布预约活动"},
                getActivity(), AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    startActivity(new Intent(getActivity(), ShareActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(getActivity(), AppointmentActivity.class));
                }
            }
        }).show();
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void stopLoading() {
        dissmiss();
    }

    @Override
    public void loadSuccess(List<Community> communities) {
        adapter.addData(CommunityUtil.sortByTime(communities));
    }

    @Override
    public void loadFail(String error) {
        toastShow(error);
    }
}
