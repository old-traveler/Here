package com.here.community.details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageView;

import com.here.R;
import com.here.adapter.CommunityDetailsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Community;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

public class CommunityDetailsActivity extends MvpActivity<CommunityDetailsPresenter> implements CommunityDetailsContract {


    @Bind(R.id.iv_movie_background)
    ImageView ivMovieBackground;
    CommunityDetailsAdapter communityDetailsAdapter;
    @Bind(R.id.rv_community_details)
    RecyclerView rvCommunityDetails;
    @Bind(R.id.fab_movie)
    ShineButton fabMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_community_details);
        ButterKnife.bind(this);
        setToolBar(R.id.toolbar);
        rvCommunityDetails.setLayoutManager(new LinearLayoutManager(this));
        List<Community> communities= new ArrayList<>();
        communities.add(new Community(CommunityDetailsAdapter.DESCRIBE));
        for (int i = 0; i < 10; i++) {
            if (i%2 == 0){
                communities.add(new Community(CommunityDetailsAdapter.SHARE));
            }else {
                communities.add(new Community(CommunityDetailsAdapter.APPOINTMENT));
            }
        }
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
}
