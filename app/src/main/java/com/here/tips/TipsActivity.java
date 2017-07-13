package com.here.tips;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.here.R;
import com.here.adapter.TipsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Tip;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TipsActivity extends MvpActivity<TipsPresenter> implements TipsContract {

    @Bind(R.id.rv_tips)
    RecyclerView rvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_tips);
        initHome();
        mvpPresenter.attachView(this);
        mvpPresenter.loadTips();
    }

    @Override
    protected TipsPresenter createPresenter() {
        return new TipsPresenter();
    }

    @Override
    public void initData(List<Tip> tips) {
        rvTips.setLayoutManager(new LinearLayoutManager(this));
        TipsAdapter tipsAdapter = new TipsAdapter(tips);
        rvTips.setAdapter(tipsAdapter);
    }
}
