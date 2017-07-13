package com.here.nickname;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.main.MainPresenter;
import com.here.personal.PersonalActivity;
import com.here.personal.PersonalPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class NicknameActivity extends MvpActivity<NicknamePresenter> implements NicknameContract {

    @Bind(R.id.iv_nickname_back)
    ImageView ivNicknameBack;
    @Bind(R.id.tv_nickname_complete)
    TextView tvNicknameComplete;
    @Bind(R.id.et_nickname)
    EditText etNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        etNickname.setText(BmobUser.getCurrentUser(User.class).getNickname());
    }

    @Override
    protected NicknamePresenter createPresenter() {
        return new NicknamePresenter();
    }

    @OnClick({R.id.iv_nickname_back, R.id.tv_nickname_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_nickname_back:
                finish();
                break;
            case R.id.tv_nickname_complete:
                tvNicknameComplete.setClickable(false);
                mvpPresenter.updateNickname();
                break;
        }
    }

    @Override
    public String getNickname() {
        return etNickname.getText().toString();
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
        tvNicknameComplete.setClickable(true);
    }
}
