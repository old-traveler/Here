package com.here.notice;

import android.os.Bundle;
import android.view.View;

import com.here.R;
import com.here.base.MvpActivity;
import com.here.util.SettingUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;

public class NoticeActivity extends MvpActivity<NoticePresenter> implements NoticeContract {

    @Bind(R.id.sv_voice)
    SwitchView svVoice;
    @Bind(R.id.sv_vibration)
    SwitchView svVibration;
    @Bind(R.id.sv_notice_show)
    SwitchView svNoticeShow;
    @Bind(R.id.sv_indicator)
    SwitchView svIndicator;
    @Bind(R.id.sv_exit_receive)
    SwitchView svExitReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_notice);
        initHome();
        svVoice.setOpened(SettingUtil.getInstance().isVoice());
        svExitReceive.setOpened(SettingUtil.getInstance().isReceiveAtExit());
        svIndicator.setOpened(SettingUtil.getInstance().isShowIndicator());
        svNoticeShow.setOpened(SettingUtil.getInstance().isShowNotice());
        svVibration.setOpened(SettingUtil.getInstance().isVibration());
    }

    @Override
    protected NoticePresenter createPresenter() {
        return new NoticePresenter();
    }

    @OnClick({R.id.sv_voice, R.id.sv_vibration, R.id.sv_notice_show, R.id.sv_indicator, R.id.sv_exit_receive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sv_voice:
                SettingUtil.getInstance().settingVoice(svVoice.isOpened());
                break;
            case R.id.sv_vibration:
                SettingUtil.getInstance().settingVibration(svVibration.isOpened());
                break;
            case R.id.sv_notice_show:
                SettingUtil.getInstance().settingNotice(svNoticeShow.isOpened());
                break;
            case R.id.sv_indicator:
                SettingUtil.getInstance().settingIndicator(svIndicator.isOpened());
                break;
            case R.id.sv_exit_receive:
                SettingUtil.getInstance().settingReceiveAtExit(svExitReceive.isOpened());
                break;
        }
    }
}
