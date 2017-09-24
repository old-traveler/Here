package com.here.follow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.CommunityDetailsAdapter;
import com.here.adapter.FindAdapter;
import com.here.base.MvpFragment;
import com.here.bean.Community;
import com.here.bean.FindImage;
import com.here.follow.join.JoinFindActivity;
import com.here.publish.appointment.AppointmentActivity;
import com.here.publish.share.ShareActivity;
import com.here.util.CommunityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by hyc on 2017/6/21 14:56
 */

public class FollowFragment extends MvpFragment<FollowPresenter> implements FollowContract {


    @Bind(R.id.rv_follow)
    RecyclerView rvFollow;
    @Bind(R.id.sl_follow)
    SmartRefreshLayout slFollow;
    private FindAdapter adapter;
    private int page = 0;
    private boolean isLoaded= false;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        ButterKnife.bind(this, view);
        mvpPresenter.attachView(this);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !isLoaded){
            initView();
            isLoaded = true;
        }else if (isVisibleToUser){
            mvpPresenter.hadJoin();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {
        slFollow.setRefreshHeader(new ClassicsHeader(getContext()));
        BallPulseFooter ballPulseFooter=new BallPulseFooter(
                getContext()).setSpinnerStyle(SpinnerStyle.Scale);
        ballPulseFooter.setPrimaryColors(Color.parseColor("#108de8"));
        slFollow.setRefreshFooter(ballPulseFooter);

        slFollow.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mvpPresenter.load(0);
                slFollow.setLoadmoreFinished(false);
            }
        });

        slFollow.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mvpPresenter.load(page);
            }
        });
        mvpPresenter.hadJoin();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        adapter = new FindAdapter(null);
        adapter.setWidth(dm.widthPixels);
        rvFollow.setLayoutManager(new StaggeredGridLayoutManager(
                2,StaggeredGridLayoutManager.VERTICAL));
        rvFollow.setAdapter(adapter);
        slFollow.autoRefresh();
        rvFollow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case SCROLL_STATE_IDLE:
                        Glide.with(getActivity()).resumeRequests();
                        break;
                    case SCROLL_STATE_DRAGGING:
                        Glide.with(getActivity()).pauseRequests();
                        break;
                    case SCROLL_STATE_SETTLING:
                        Glide.with(getActivity()).resumeRequests();
                        break;
                }
            }
        });

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

    @Override
    public void loadFail(String error) {
        if (slFollow.isRefreshing()){
            slFollow.finishRefresh();
        }else if (slFollow.isLoading()){
            slFollow.finishLoadmore();
        }
        toastShow(error);
    }

    @Override
    public void reminderJoin() {
        if (getUserVisibleHint()){
            new AlertView("温馨提示", "加入发现，上传您的专属照片。\n让附近的人认识一下你吧！" +
                    "\n拒绝后，可在设置里开启", "确定",new String[]{"拒绝"}, null, getContext(),
                    AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == -1){
                        startActivity(new Intent(getActivity()
                                , JoinFindActivity.class));
                    }else if (position == 0){
                        mvpPresenter.refuse();
                    }
                }
            }).show();
        }
    }

    @Override
    public void refreshData(List<FindImage> images) {
        adapter.setFindImages(images);
        page = 1;
        slFollow.finishRefresh();
    }

    @Override
    public void addData(List<FindImage> images) {
        adapter.addFindImage(images);
        page++;
        if (images.size() == 0){
            slFollow.setLoadmoreFinished(true);
        }
        slFollow.finishLoadmore();
    }


}
