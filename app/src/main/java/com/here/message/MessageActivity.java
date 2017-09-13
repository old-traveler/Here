package com.here.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.here.R;
import com.here.adapter.MessageAdapter;
import com.here.base.MvpActivity;
import com.here.search.SearchActivity;
import com.here.server.ChatInfoUpdateServer;
import com.here.util.ImUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;

public class MessageActivity extends MvpActivity<MessagePresenter> implements MessageContract {

    @Bind(R.id.rv_message)
    RecyclerView rvMessage;
    @Bind(R.id.sl_message)
    SwipeRefreshLayout slMessage;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_message);
        initHome();
        if (ImUtil.isConnected){
            messageAdapter = new MessageAdapter(BmobIM.getInstance().loadAllConversation());
        }else {
            messageAdapter = new MessageAdapter(null);
        }
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        rvMessage.setItemAnimator(new DefaultItemAnimator());
        rvMessage.setAdapter(messageAdapter);
        slMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ImUtil.isConnected){
                    messageAdapter.setNewData(BmobIM.getInstance().loadAllConversation());
                }
                slMessage.setRefreshing(false);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (messageAdapter != null && ImUtil.isConnected){
            messageAdapter.setNewData(BmobIM.getInstance().loadAllConversation());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BmobIMMessage event) {
        if (ImUtil.isConnected){
            messageAdapter.setNewData(BmobIM.getInstance().loadAllConversation());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.message_add){
            startActivity(new Intent(MessageActivity.this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter();
    }


    @Override
    public void loadingData() {

    }
}
