package com.here.record.publish;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.adapter.CommunityAdapter;
import com.here.adapter.CommunityDetailsAdapter;
import com.here.adapter.RecordAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Community;
import com.here.bean.ImActivity;
import com.here.bean.User;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PublishRecordActivity extends MvpActivity<PublishRecordPresenter> implements PublishRecordContract {

    @Bind(R.id.rv_publish_record)
    RecyclerView rvPublishRecord;
    @Bind(R.id.sl_publish_record)
    SmartRefreshLayout slPublishRecord;
    private CommunityDetailsAdapter communityAdapter;
    private RecordAdapter recordAdapter;
    private int page = 0;
    private int kind = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_record);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_publish_record);
        mvpPresenter.attachView(this);
        initHome();
        initView();
    }

    private void initView() {
        rvPublishRecord.setLayoutManager(new LinearLayoutManager(this));
        communityAdapter = new CommunityDetailsAdapter(null,this);
        recordAdapter = new RecordAdapter(null);
        slPublishRecord.setRefreshHeader(new ClassicsHeader(this));
        BallPulseFooter ballPulseFooter=new BallPulseFooter(this)
                .setSpinnerStyle(SpinnerStyle.Scale);
        ballPulseFooter.setPrimaryColors(Color.parseColor("#108de8"));
        slPublishRecord.setRefreshFooter(ballPulseFooter);
        slPublishRecord.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                switch (kind){
                    case 0:mvpPresenter.queryImActivityRecord(0);
                        break;
                    case 1:mvpPresenter.queryAppointmentRecord(0);
                        break;
                    case 2:mvpPresenter.queryShareRecord(0);
                        break;
                }
                slPublishRecord.setLoadmoreFinished(false);
            }
        });
        slPublishRecord.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                switch (kind){
                    case 0:mvpPresenter.queryImActivityRecord(page);
                        break;
                    case 1:mvpPresenter.queryAppointmentRecord(page);
                        break;
                    case 2:mvpPresenter.queryShareRecord(page);
                        break;
                }
            }
        });

        slPublishRecord.autoRefresh();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_record,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish_record_kind){
            selectKind();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectKind(){
        new AlertView("类型选择", null, "取消", null, new String[]{
                "即时活动","预约活动","心情分享"}, this, AlertView
                .Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position > -1){
                    kind = position;
                    rvPublishRecord.removeAllViews();
                    slPublishRecord.autoRefresh();
                }
            }
        }).show();
    }

    @Override
    protected PublishRecordPresenter createPresenter() {
        return new PublishRecordPresenter();
    }


    @Override
    public void setShareRecord(List<Community> communities) {
        rvPublishRecord.setAdapter(communityAdapter);
        communityAdapter.setData(communities);
        slPublishRecord.finishRefresh();
        page = 1;
    }

    @Override
    public void addShareRecord(List<Community> communities) {
        communityAdapter.addRecord(communities);
        slPublishRecord.finishLoadmore();
        page ++ ;
        if (communities.size() == 0){
            slPublishRecord.setLoadmoreFinished(true);
        }
    }

    @Override
    public void setAppointmentRecord(List<Community> communities) {
        communityAdapter.setData(communities);
        rvPublishRecord.setAdapter(communityAdapter);
        slPublishRecord.finishRefresh();
        page = 1;

    }

    @Override
    public void addAppointmentRecord(List<Community> communities) {
        communityAdapter.addRecord(communities);
        slPublishRecord.finishLoadmore();
        page ++ ;
        if (communities.size() == 0){
            slPublishRecord.setLoadmoreFinished(true);
        }
    }

    @Override
    public void setImActivityRecord(List<ImActivity> imActivities) {
        recordAdapter.setNewData(imActivities);
        rvPublishRecord.setAdapter(recordAdapter);
        slPublishRecord.finishRefresh();
        page = 1;
    }

    @Override
    public void addImActivityRecord(List<ImActivity> imActivities) {
        recordAdapter.addData(imActivities);
        slPublishRecord.finishLoadmore();
        page ++ ;
        if (imActivities.size() == 0){
            slPublishRecord.setLoadmoreFinished(true);
        }
    }

    @Override
    public void loadFail(String error) {
        if (slPublishRecord.isRefreshing()){
            slPublishRecord.finishRefresh();
        }else if (slPublishRecord.isLoading()){
            slPublishRecord.finishLoadmore();
        }
        toastShow(error);
    }

    @Override
    public User getPublisher() {
        return (User) getIntent().getSerializableExtra("publisher");
    }


}
