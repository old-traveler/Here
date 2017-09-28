package com.here.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.login.LoginActivity;
import com.here.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import qiu.niorgai.StatusBarCompat;

public class SplashActivity extends MvpActivity<SplashPresenter> implements SplashContract {

    @Bind(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        showSplashImage();

    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }


    @Override
    public void showSplashImage() {
        Glide.with(this)
                .load(R.drawable.splash_one)
                .into(ivSplash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLogin();
            }
        }, 1500);
    }

    @Override
    public void isLogin() {
        if (BmobUser.getCurrentUser(User.class)!=null){
            startActivity(new Intent(this,MainActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }
}
