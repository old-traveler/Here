package com.here.privacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.login.LoginActivity;
import com.here.setting.SettingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import cn.bmob.v3.BmobUser;

public class PrivacyActivity extends MvpActivity<PrivacyPresenter> implements PrivacyContract {


    @Bind(R.id.sv_age)
    SwitchView svAge;
    @Bind(R.id.sv_sex)
    SwitchView svSex;
    @Bind(R.id.sv_phone)
    SwitchView svPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_privacy);
        initHome();
        mvpPresenter.attachView(this);
        initData();
    }

    private void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        svAge.setOpened(user.isShowAge());
        svSex.setOpened(user.isShowDataOfBirth());
        svPhone.setOpened(user.isShowNumber());
    }

    @Override
    protected PrivacyPresenter createPresenter() {
        return new PrivacyPresenter();
    }


    @Override
    public User getUpdateUser() {
        User user = BmobUser.getCurrentUser(User.class);
        user.setShowAge(svAge.isOpened());
        user.setShowDataOfBirth(svSex.isOpened());
        user.setShowNumber(svPhone.isOpened());
        return user;
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
    public void update() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user.isShowAge() == svAge.isOpened()&&user
                .isShowDataOfBirth() == svSex.isOpened()
                &&user.isShowNumber() == svPhone.isOpened()){
            finish();
        }else {
            new AlertView("温馨提示", "是否修改隐私设置", "确定", new String[]{"取消"}, null, this,
                    AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position==-1){
                        mvpPresenter.update();
                    }else {
                        finish();
                    }
                }
            }).show();

        }

    }

    @Override
    public void updateSuccess() {
        toastShow("修改成功");
        finish();
    }

    @Override
    public void updateFail(String error) {
        toastShow(error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            update();
        }
        return true;
    }
}
