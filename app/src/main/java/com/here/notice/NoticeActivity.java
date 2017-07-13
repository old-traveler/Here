package com.here.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.here.R;
import com.here.base.MvpActivity;

public class NoticeActivity extends MvpActivity<NoticePresenter> implements NoticeContract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setToolBar(R.id.tb_notice);
        initHome();
    }

    @Override
    protected NoticePresenter createPresenter() {
        return new NoticePresenter();
    }
}
