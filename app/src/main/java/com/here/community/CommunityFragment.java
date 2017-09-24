package com.here.community;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.adapter.CommunityAdapter;
import com.here.base.MvpFragment;
import com.here.bean.Community;
import com.here.bean.Propaganda;
import com.here.publish.appointment.AppointmentActivity;
import com.here.publish.share.ShareActivity;
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
import butterknife.OnClick;

/**
 * Created by hyc on 2017/6/21 14:59
 */

public class CommunityFragment extends MvpFragment<CommunityPresenter> implements CommunityContract {


    @Bind(R.id.rv_community)
    RecyclerView rvCommunity;
    @Bind(R.id.sl_community)
    SmartRefreshLayout slCommunity;
    @Bind(R.id.fb_add_activity)
    FloatingActionButton fbAddActivity;
    CommunityAdapter communityAdapter;
    private boolean isLoad = false;
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, view);
        mvpPresenter.attachView(this);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !isLoad){
            isLoad = true;
            initView();
            slCommunity.autoRefresh();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void initView() {
        List<Community> list =new ArrayList<>();
        Community c= new Community();
        c.setType(Community.TYPE_VIEW_PAGE);
        Propaganda[] propagandas = new Propaganda[6];
        propagandas[0] = new Propaganda();
        propagandas[1] = new Propaganda();
        propagandas[2] = new Propaganda();
        propagandas[3] = new Propaganda();
        propagandas[4] = new Propaganda();
        propagandas[5] = new Propaganda();
        propagandas[0].setDescribe("给自己来一场刺激的周末吧~");
        propagandas[0].setImage(R.drawable.page_1);
        propagandas[1].setDescribe("别再一人独自吃外卖了");
        propagandas[1].setImage(R.drawable.page_2);
        propagandas[2].setDescribe("一起打球，一起流汗！");
        propagandas[2].setImage(R.drawable.page_3);
        propagandas[3].setDescribe("拥抱自然的芬芳");
        propagandas[3].setImage(R.drawable.page_4);
        propagandas[4].setDescribe("杀出你的天下");
        propagandas[4].setImage(R.drawable.page_5);
        propagandas[5].setDescribe("陪我去看一场烟花");
        propagandas[5].setImage(R.drawable.page_6);
        Community.setPropagandas(propagandas);
        Community com =new Community();
        com.setType(Community.TYPE_COMMUNITY);
        list.add(com);
        list.add(c);
        Community tip = new Community();
        tip.setType(Community.TYPE_TIPS);
        list.add(tip);
        communityAdapter = new CommunityAdapter(list,getActivity());
        rvCommunity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCommunity.setAdapter(communityAdapter);
        slCommunity.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mvpPresenter.loadCommunityData(0);
                slCommunity.setLoadmoreFinished(false);
            }
        });

        slCommunity.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mvpPresenter.loadCommunityData(page);
            }
        });
        slCommunity.setRefreshHeader(new ClassicsHeader(getContext()));
        BallPulseFooter ballPulseFooter=new BallPulseFooter(
                getContext()).setSpinnerStyle(SpinnerStyle.Scale);
        ballPulseFooter.setPrimaryColors(Color.parseColor("#108de8"));
        slCommunity.setRefreshFooter(ballPulseFooter);



    }

    @Override
    protected CommunityPresenter createPresenter() {
        return new CommunityPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setRecommend(List<Community> communities) {
        if (communityAdapter.getItemCount() > 3){
            communityAdapter.restore();
        }
        communityAdapter.setData(communities);
        slCommunity.finishRefresh();
        page = 1;
    }

    @Override
    public void fail(String error) {
        if (slCommunity.isRefreshing()){
            slCommunity.finishRefresh();
        }else if (slCommunity.isLoading()){
            slCommunity.finishLoadmore();
        }
        toastShow(error);
    }

    @Override
    public void addRecommend(List<Community> communities) {
        if (communities.size() > 0){
            communityAdapter.addData(communities);
        }else {
            slCommunity.setLoadmoreFinished(true);
        }
        page++;
        slCommunity.finishLoadmore();
    }

    public void slideToTop(){
        if (rvCommunity != null){
            rvCommunity.scrollToPosition(0);
        }
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
}
