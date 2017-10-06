package com.here.blacklist;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.here.R;
import com.here.adapter.MyFollowAdapter;
import com.here.base.MvpActivity;
import com.here.bean.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlacklistActivity extends MvpActivity<BlacklistPresenter> implements BlacklistContract {


    @Bind(R.id.rv_blacklist)
    RecyclerView rvBlacklist;
    @Bind(R.id.tv_load_error)
    TextView tvLoadError;
    private MyFollowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        mvpPresenter.loadBlacklist();
        setToolBar(R.id.tb_blacklist);
        initHome();
    }

    @Override
    protected BlacklistPresenter createPresenter() {
        return new BlacklistPresenter();
    }

    @OnClick(R.id.tv_load_error)
    public void onViewClicked() {
        mvpPresenter.loadBlacklist();
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
    public void setBlacklist(List<User> user) {
        adapter = new MyFollowAdapter(user);
        rvBlacklist.setLayoutManager(new LinearLayoutManager(this));
        rvBlacklist.setItemAnimator(new DefaultItemAnimator());
        rvBlacklist.setAdapter(adapter);
        if (tvLoadError.getVisibility() == View.VISIBLE){
            tvLoadError.setVisibility(View.GONE);
        }
        adapter.setListener(new MyFollowAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                mvpPresenter.removeBlacklist(position);
            }
        });
    }

    @Override
    public void loadFail(String error) {
        toastShow(error);
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTips(String error) {
        toastShow(error);
    }

    @Override
    public void removeBlacklist(int position) {
        adapter.remove(position);
    }
}
