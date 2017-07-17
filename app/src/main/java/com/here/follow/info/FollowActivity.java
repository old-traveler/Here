package com.here.follow.info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.MyFollowAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Follow;
import com.here.bean.User;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowActivity extends MvpActivity<FollowPresenter> implements FollowContract {

    @Bind(R.id.tb_follow)
    Toolbar tbFollow;
    @Bind(R.id.cv_follow_head)
    CircleImageView cvFollowHead;
    @Bind(R.id.tv_follow_count)
    TextView tvFollowCount;
    @Bind(R.id.tv_fans_count)
    TextView tvFansCount;
    @Bind(R.id.tv_my_follow)
    TextView tvMyFollow;
    @Bind(R.id.view_my_follow)
    View viewMyFollow;
    @Bind(R.id.rl_my_follow)
    RelativeLayout rlMyFollow;
    @Bind(R.id.tv_my_fans)
    TextView tvMyFans;
    @Bind(R.id.view_my_fans)
    View viewMyFans;
    @Bind(R.id.rl_my_fans)
    RelativeLayout rlMyFans;
    @Bind(R.id.rv_my_follow)
    RecyclerView rvMyFollow;
    private MyFollowAdapter myFollowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        myFollowAdapter = new MyFollowAdapter(null);
        rvMyFollow.setLayoutManager(new LinearLayoutManager(this));
        rvMyFollow.setAdapter(myFollowAdapter);
        mvpPresenter.queryMyFollow();
        Glide.with(this)
                .load(BmobUser.getCurrentUser(User.class).getHeadImageUrl())
                .into(cvFollowHead);
        setToolBar(R.id.tb_follow);
        initHome();
    }

    @Override
    protected FollowPresenter createPresenter() {
        return new FollowPresenter();
    }

    @OnClick({R.id.rl_my_fans, R.id.rl_my_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_my_fans:
                viewMyFans.setVisibility(View.VISIBLE);
                tvMyFans.setTextColor(getResources().getColor(R.color.color_accent));
                tvMyFollow.setTextColor(getResources().getColor(R.color.share_text));
                viewMyFollow.setVisibility(View.GONE);
                mvpPresenter.queryMyFans();
                break;
            case R.id.rl_my_follow:
                viewMyFans.setVisibility(View.GONE);
                tvMyFans.setTextColor(getResources().getColor(R.color.share_text));
                tvMyFollow.setTextColor(getResources().getColor(R.color.color_accent));
                viewMyFollow.setVisibility(View.VISIBLE);
                mvpPresenter.queryMyFollow();
                break;
        }
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
    public void loadMyFans(List<Follow> fans) {
        List<User> users = new ArrayList<>();
        for (Follow fan : fans) {
            users.add(fan.getUser());
        }
        myFollowAdapter.setNewData(users);
        tvFansCount.setText("粉丝："+users.size());

    }

    @Override
    public void loadMyFollow(List<Follow> follow) {
        List<User> users = new ArrayList<>();
        for (Follow fan : follow) {
            users.add(fan.getFollowUser());
        }
        myFollowAdapter.setNewData(users);
        tvFollowCount.setText("关注："+users.size());
    }

    @Override
    public void loadFail(String error) {
        toastShow(error);
    }
}
