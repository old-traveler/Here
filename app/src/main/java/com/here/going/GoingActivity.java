package com.here.going;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.here.R;
import com.here.adapter.JoinAdapter;
import com.here.base.MvpActivity;
import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;
import com.here.login.LoginActivity;
import com.here.personal.other.OtherInfoActivity;
import com.here.setting.SettingActivity;
import com.here.util.ImActivityUtil;
import com.here.util.JoinUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class GoingActivity extends MvpActivity<GoingPresenter> implements GoingContract {

    @Bind(R.id.rv_join_user)
    RecyclerView rvJoinUser;
    @Bind(R.id.arc_progress)
    ArcProgress arcProgress;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.tv_going_title)
    TextView tvGoingTitle;
    @Bind(R.id.tv_going_kind)
    TextView tvGoingKind;
    @Bind(R.id.tv_going_describe)
    TextView tvGoingDescribe;
    @Bind(R.id.btn_delete_going)
    Button btnDeleteGoing;
    @Bind(R.id.tv_publisher_phone)
    TextView tvPublisherPhone;
    @Bind(R.id.btn_contract_going)
    Button btnContractGoing;
    @Bind(R.id.tb_going)
    Toolbar tbGoing;
    @Bind(R.id.rl_publisher_phone)
    RelativeLayout rlPublisherPhone;
    @Bind(R.id.tv_join_number)
    TextView tvJoinNumber;
    private boolean isSelf = false;
    private JoinAdapter joinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_going);
        initHome();
        ImActivity imActivity = ImActivityUtil.getImActivityInfo(BmobUser
                .getCurrentUser(User.class));
        if (!BmobUser.getCurrentUser().getObjectId().equals(ImActivityUtil.getPublisherId())) {
            btnDeleteGoing.setVisibility(View.GONE);
        } else {
            rlPublisherPhone.setVisibility(View.GONE);
            btnContractGoing.setVisibility(View.GONE);
            isSelf = true;
        }
        mvpPresenter.initGoing();
        rvJoinUser.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        joinAdapter = new JoinAdapter(null);
        rvJoinUser.setAdapter(joinAdapter);
    }

    @Override
    protected GoingPresenter createPresenter() {
        return new GoingPresenter();
    }


    @Override
    public void setGoingData(ImActivity imActivity) {
        tvOverTime.setText("活动将于" + imActivity.getOverTime().split("-")[3] + "结束");
        tvGoingTitle.setText(imActivity.getTitle());
        tvGoingDescribe.setText(imActivity.getDescribe());
        tvGoingKind.setText(imActivity.getKind());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_going, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_going){
            mvpPresenter.refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void setSurplusTime(int surplus, int time) {
        final ValueAnimator anim = ObjectAnimator.ofInt(100, surplus).setDuration((long) 500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcProgress.setProgress((Integer) animation.getAnimatedValue());
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim.start();
            }
        }, 1000);
        arcProgress.setBottomText("活动已进行" + time + "分钟");
    }

    @Override
    public void loadPhoneNumber(String number) {
        tvPublisherPhone.setText(number);
    }

    @Override
    public void loadJoinUserInfo(List<Join> joins,ImActivity imActivity) {
        tvJoinNumber.setText(joins.size()+"/"+imActivity.getNumber());
        joins.add(new Join());
        joinAdapter.setNewData(joins);
    }

    @Override
    public boolean isSelf() {
        return isSelf;
    }

    @Override
    public void fail(String error) {
        toastShow(error);
    }

    @Override
    public void deleteSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertView("提示", "删除成功", null
                        , new String[]{"确定"}, null, GoingActivity
                        .this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        finish();
                    }
                }).show();
            }
        }, 500);

    }

    @Override
    public void confirmDelete() {
        new AlertView("温馨提示", "确认删除活动", "确定", new String[]{"取消"}, null, this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == -1) {
                    mvpPresenter.delete();
                }
            }
        }).show();
    }

    @Override
    public void contract(User user) {
        Intent intent = new Intent(this, OtherInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("other",user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.btn_delete_going, R.id.btn_contract_going})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete_going:
                confirmDelete();
                break;
            case R.id.btn_contract_going:
                mvpPresenter.contract();
                break;
        }
    }
}
