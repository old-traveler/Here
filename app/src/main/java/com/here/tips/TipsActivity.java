package com.here.tips;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.adapter.TipsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Tip;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TipsActivity extends MvpActivity<TipsPresenter> implements TipsContract {

    @Bind(R.id.rv_tips)
    RecyclerView rvTips;

    private  TipsAdapter tipsAdapter;

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
        tipsAdapter = new TipsAdapter(tips);
        rvTips.setAdapter(tipsAdapter);
    }

    @Override
    public String[] getTips() {
        List<String> tips = new ArrayList<>();
        for (Tip tip : tipsAdapter.getData()) {
            if (tip.isHave()){
                tips.add(tip.getName());
            }
        }
        String[] tipStr = new String[tips.size()];
        for (int i = 0; i < tips.size(); i++) {
            tipStr[i] = tips.get(i);
        }
        return tipStr;
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
    public void loadSuccess() {
        toastShow("修改成功");
        finish();
    }

    @Override
    public void loadFail(String error) {
        toastShow(error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            new AlertView("温馨提示", "是否修改隐私设置", "确定", new String[]{
                    "取消"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position==-1){
                        mvpPresenter.uploadTips();
                    }else {
                        finish();
                    }
                }
            }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
