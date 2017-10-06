package com.here.binding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindInfoActivity extends MvpActivity<BindInfoPresenter> implements BindInfoContract {

    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.et_id_card)
    EditText etIdCard;
    @Bind(R.id.btn_next_step)
    Button btnNextStep;
    @Bind(R.id.tv_deal)
    TextView tvDeal;
    @Bind(R.id.cb_deal)
    CheckBox cbDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_info);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_binding);
        mvpPresenter.attachView(this);
        initHome();
    }

    @Override
    protected BindInfoPresenter createPresenter() {
        return new BindInfoPresenter();
    }

    @Override
    public void showUserDeal() {
        new AlertView(getString(R.string.tips_deal), getString(R.string.user_deal)
                , getString(R
                .string.agree), new String[]{getString(R.string.cancel)}, null, this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == -1) {
                    cbDeal.setChecked(true);
                }
            }
        }).show();
    }

    @Override
    public boolean isAgreeDeal() {
        return cbDeal.isChecked();
    }

    @Override
    public boolean isFilled() {
        return !(TextUtils.isEmpty(etName.getText().toString())
                || TextUtils.isEmpty(etAddress.getText().toString())
                || TextUtils.isEmpty(etIdCard.getText().toString())
                || TextUtils.isEmpty(etNickname.getText().toString()));
    }

    @Override
    public boolean idIsRight() {
        return etIdCard.getText().toString().length() == 18;
    }

    @Override
    public User getUser() {
        User user = new User();
        user.setName(etName.getText().toString());
        user.setIdCard(etIdCard.getText().toString());
        user.setHomeAddress(etAddress.getText().toString());
        user.setNickname(etNickname.getText().toString());
        return user;
    }

    @Override
    public void showTips(int msg) {
        toastShow(msg);
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
    public void bindSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        finish();
    }

    @Override
    public void bindFail(String error) {
        toastShow(error);
    }

    @OnClick({R.id.btn_next_step, R.id.tv_deal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next_step:
                mvpPresenter.checkUserInfo();
                break;
            case R.id.tv_deal:
                showUserDeal();
                break;
        }
    }
}
