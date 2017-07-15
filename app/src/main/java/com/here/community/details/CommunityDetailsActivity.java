package com.here.community.details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.CommunityDetailsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Community;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

public class CommunityDetailsActivity extends MvpActivity<CommunityDetailsPresenter> implements CommunityDetailsContract {


    CommunityDetailsAdapter communityDetailsAdapter;
    @Bind(R.id.rv_community_details)
    RecyclerView rvCommunityDetails;
    @Bind(R.id.fab_movie)
    ShineButton fabMovie;
    @Bind(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;
    @Bind(R.id.iv_community_background)
    ImageView ivCommunityBackground;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_community_details);
        ButterKnife.bind(this);
        setToolBar(R.id.toolbar);
        mvpPresenter.attachView(this);
        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        initCommunityDetails();
        mvpPresenter.loadCommunityData();
    }

    private void initCommunityDetails() {
        rvCommunityDetails.setLayoutManager(new LinearLayoutManager(this));
        List<Community> communities = new ArrayList<>();
        Community describe = new Community();
        describe.setType(CommunityDetailsAdapter.DESCRIBE);
        switch (getIntent().getStringExtra("kind")) {
            case "逛街":
                Glide.with(this)
                        .load(R.drawable.gw)
                        .into(ivCommunityBackground);
                describe.setDescribe("不想一个人购物..   想找个人做参谋..   不知道去哪逛街..\n这些问题逛街社区可以帮你解决~\n快加入我们吧  --逛街社区");
                break;
            case "运动":
                Glide.with(this)
                        .load(R.drawable.yd)
                        .into(ivCommunityBackground);
                describe.setDescribe("运动是一件需要坚持的事情\ncome on\n一起流汗、一起热血吧  --运动社区");
                break;
            case "电影":
                Glide.with(this)
                        .load(R.drawable.dy)
                        .into(ivCommunityBackground);
                describe.setDescribe("自己一个人去电影院看电影不尴尬吗？\n没有陪伴你的人？\n那就来这找个人吧  --电影社区");
                break;
            case "游戏":
                Glide.with(this)
                        .load(R.drawable.yx)
                        .into(ivCommunityBackground);
                describe.setDescribe("来来来...\n五黑高速列车\n老司机快上车  --游戏社区");
                break;
            case "美食":
                Glide.with(this)
                        .load(R.drawable.ms)
                        .into(ivCommunityBackground);
                describe.setDescribe("一个人，再好的美食也会乏味\n真正的美食\n需要大家一起来分享  --美食社区");
                break;
            case "桌游":
                Glide.with(this)
                        .load(R.drawable.zy)
                        .into(ivCommunityBackground);
                describe.setDescribe("天气不好，不想出门？\n只想窝在一个地方？\n叫上一群人一起进入桌游的世界吧  --桌游社区");
                break;
            case "唱歌":
                Glide.with(this)
                        .load(R.drawable.cg)
                        .into(ivCommunityBackground);
                describe.setDescribe("烦闷的情绪无处发泄？\n孤单一人的时候想找人出门唱歌？\n唱歌社区 --谨此献给喜欢唱歌的人");
                break;
            case "美容":
                Glide.with(this)
                        .load(R.drawable.mr)
                        .into(ivCommunityBackground);
                describe.setDescribe("好看的皮囊太多\n有趣的灵魂太少\n来这儿成为那个两者兼具的人吧  --美容社区");
                break;
            case "户外":
                Glide.with(this)
                        .load(R.drawable.hw)
                        .into(ivCommunityBackground);
                describe.setDescribe("车水马龙，人流济济，让人厌倦\n来吧，一起走向户外\n拥抱一个清新的大自然  --户外社区");
                break;
            case "酒吧":
                Glide.with(this)
                        .load(R.drawable.jb)
                        .into(ivCommunityBackground);
                describe.setDescribe("工作时受到的委屈、生活中受到的挫折\n找不到人陪你宣泄？\n人生过于沉闷，来这寻找属于你的闪光点吧  --酒吧社区");
                break;
            case "棋牌":
                Glide.with(this)
                        .load(R.drawable.xp)
                        .into(ivCommunityBackground);
                describe.setDescribe("找不到陪你下棋的人？\n想遇见一个能与你匹敌的人？\n来这和同道之人分享你的真知灼见  --棋牌社区");
                break;
            case "其他":
                Glide.with(this)
                        .load(R.drawable.qt)
                        .into(ivCommunityBackground);
                describe.setDescribe("即使生活没有确切定义\n美好的事物依旧如期而至\n来这儿遇见那些美好的人儿吧  --其他社区");
                break;
        }
        communities.add(describe);
        communityDetailsAdapter = new CommunityDetailsAdapter(communities);
        rvCommunityDetails.setAdapter(communityDetailsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_community_details, menu);
        return true;
    }

    @Override
    protected CommunityDetailsPresenter createPresenter() {
        return new CommunityDetailsPresenter();
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
        communityDetailsAdapter.addData(communities);
    }

    @Override
    public void fail(String error) {
        toastShow(error);
    }

    @Override
    public String getKind() {
        return getIntent().getStringExtra("kind");
    }
}
