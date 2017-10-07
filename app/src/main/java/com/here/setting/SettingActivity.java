package com.here.setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.account.AccountActivity;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.blacklist.BlacklistActivity;
import com.here.follow.join.JoinFindActivity;
import com.here.login.LoginActivity;
import com.here.notice.NoticeActivity;
import com.here.password.PasswordActivity;
import com.here.phone.PhoneActivity;
import com.here.util.FindUtil;
import com.here.util.ImUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


public class SettingActivity extends MvpActivity<SettingPresenter> implements SettingContract {

    @Bind(R.id.tb_setting)
    Toolbar tbSetting;
    @Bind(R.id.rl_account_manage)
    RelativeLayout rlAccountManage;
    @Bind(R.id.rl_phone_number)
    RelativeLayout rlPhoneNumber;
    @Bind(R.id.rl_message_notice)
    RelativeLayout rlMessageNotice;
    @Bind(R.id.rl_black_list)
    RelativeLayout rlBlackList;
    @Bind(R.id.rl_current_version)
    RelativeLayout rlCurrentVersion;
    @Bind(R.id.rl_version_update)
    RelativeLayout rlVersionUpdate;
    @Bind(R.id.tv_exit_login)
    TextView tvExitLogin;
    @Bind(R.id.tv_user_number)
    TextView tvUserNumber;
    @Bind(R.id.rl_update_password)
    RelativeLayout rlUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_setting);
        initHome();
        initNumber();
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @OnClick({R.id.rl_account_manage, R.id.rl_phone_number, R.id.rl_message_notice,R.id.rl_find_setting, R.id.rl_update_password, R.id.rl_black_list, R.id.rl_current_version, R.id.rl_version_update, R.id.tv_exit_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_account_manage:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.rl_phone_number:
                startActivity(new Intent(this, PhoneActivity.class));
                break;
            case R.id.rl_update_password:
                startActivity(new Intent(this, PasswordActivity.class));
                break;
            case R.id.rl_message_notice:
                startActivity(new Intent(this, NoticeActivity.class));
                break;
            case R.id.rl_black_list:
                startActivity(new Intent(this, BlacklistActivity.class));
                break;
            case R.id.rl_current_version:
                break;
            case R.id.rl_version_update:
                break;
            case R.id.tv_exit_login:
                new AlertView("温馨提示", "是否退出当前账号", "确定", new String[]{"取消"}, null, this,
                        AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == -1) {
                            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                            BmobUser.logOut();
                            FindUtil.cleanRecord();
                            ImUtil.disconnect();
                            finish();
                            finishAll();
                        }
                    }
                }).show();
                break;
            case R.id.rl_find_setting:
                Intent intent = new Intent(this, JoinFindActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void initNumber() {
        String number = BmobUser.getCurrentUser(User.class).getMobilePhoneNumber();
        if (!TextUtils.isEmpty(number)) {
            tvUserNumber.setText(number.substring(0, 3) + "****" + number.substring(7));
        } else {
            tvUserNumber.setText(R.string.no_bind);
        }
    }



}
