package com.here.introduction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.main.MainPresenter;
import com.here.personal.PersonalPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class IntroductionActivity extends MvpActivity<IntroductionPresenter> implements IntroductionContract {

    @Bind(R.id.tv_complete)
    TextView tvComplete;
    @Bind(R.id.et_introduction)
    EditText etIntroduction;
    @Bind(R.id.iv_introduction_back)
    ImageView ivIntroductionBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        etIntroduction.setText(BmobUser.getCurrentUser(User.class).getIntroduction());
    }

    @Override
    protected IntroductionPresenter createPresenter() {
        return new IntroductionPresenter();
    }

    @Override
    public String getIntroduction() {
        return etIntroduction.getText().toString();
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
    public void updateSuccess() {
        MainPresenter.isUpdate=true;
        PersonalPresenter.isNeedRefreshUserData=true;
        finish();
    }

    @Override
    public void updateFail(String error) {
        toastShow(error);
    }


    @OnClick({R.id.iv_introduction_back, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_introduction_back:
                finish();
                break;
            case R.id.tv_complete:
                mvpPresenter.updateIntroduction();
                break;
        }
    }
}
