package com.here.apply;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.util.ImUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import de.hdodenhof.circleimageview.CircleImageView;

public class ApplyActivity extends MvpActivity<ApplyPresenter> implements ApplyContract {

    @Bind(R.id.cv_apply_head)
    CircleImageView cvApplyHead;
    @Bind(R.id.tv_apply_nickname)
    TextView tvApplyNickname;
    @Bind(R.id.tv_apply_username)
    TextView tvApplyUsername;
    @Bind(R.id.btn_agree)
    Button btnAgree;
    @Bind(R.id.btn_refuse)
    Button btnRefuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_apply);
        initHome();
        mvpPresenter.loadingData();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra("id",0));
    }

    @Override
    protected ApplyPresenter createPresenter() {
        return new ApplyPresenter();
    }

    @Override
    public void fail(String error) {
        toastShow(error);
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
    public MessageEvent getApplyUserInfo() {
        return (MessageEvent) getIntent().getSerializableExtra("apply");
    }

    @Override
    public void respondSuccess(String msg) {
        new AlertView("提示", msg, null
                , new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                finish();
            }
        }).show();
    }

    @Override
    public void loadUserInfo(BmobIMUserInfo info, String username) {
        Glide.with(this)
                .load(info.getAvatar())
                .into(cvApplyHead);
        tvApplyNickname.setText(info.getName());
        tvApplyUsername.setText("账号："+username);

    }

    @OnClick({R.id.btn_agree, R.id.btn_refuse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_agree:
                mvpPresenter.agree();
                break;
            case R.id.btn_refuse:
                mvpPresenter.refuse();
                break;
        }
    }
}
