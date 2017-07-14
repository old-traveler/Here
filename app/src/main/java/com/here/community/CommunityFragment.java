package com.here.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.here.R;
import com.here.adapter.CommunityAdapter;
import com.here.base.MvpFragment;
import com.here.bean.Community;
import com.here.bean.Propaganda;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyc on 2017/6/21 14:59
 */

public class CommunityFragment extends MvpFragment<CommunityPresenter> implements CommunityContract {


    @Bind(R.id.rv_community)
    RecyclerView rvCommunity;
    @Bind(R.id.sl_community)
    SmartRefreshLayout slCommunity;
    CommunityAdapter communityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, view);
        mvpPresenter.attachView(this);
        initView();
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
        list.add(c);
        Community com =new Community();
        com.setType(Community.TYPE_COMMUNITY);
        list.add(com);
        Community tip = new Community();
        tip.setType(Community.TYPE_TIPS);
        list.add(tip);
        for (int i = 0; i < 10; i++) {
            Community tis = new Community();
            if (i%2 == 0){
                tis.setType(Community.TYPE_SHARE);
            }else {
                tis.setType(Community.TYPE_APPOINTMENT);
            }
            list.add(tis);
        }
        communityAdapter = new CommunityAdapter(list);
        rvCommunity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCommunity.setAdapter(communityAdapter);
        return view;
    }

    private void initView() {
        slCommunity.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });

        slCommunity.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });


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
}
