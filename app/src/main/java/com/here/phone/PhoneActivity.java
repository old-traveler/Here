package com.here.phone;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class PhoneActivity extends MvpActivity<PhonePresenter> implements PhoneContract {

    @Bind(R.id.btn_update_phone)
    Button btnUpdatePhone;
    @Bind(R.id.tv_number)
    TextView tvNumber;




    private AlertView mAlertViewExt;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_phone);
        initHome();
        initNumber();
    }

    @Override
    protected PhonePresenter createPresenter() {
        return new PhonePresenter();
    }

    @OnClick(R.id.btn_update_phone)
    public void onViewClicked() {
        mvpPresenter.newPhoneNumber();
    }

    @Override
    public void changePhoneNumber() {

        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setHint("please input new tel");
        mAlertViewExt = new AlertView("提示", "请输入你的新号码！", "取消", null, new String[]{"下一步"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    mvpPresenter.verifyNumber(etName.getText().toString());
                }
            }
        });
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                boolean isOpen = imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
            }
        });
        mAlertViewExt.addExtView(extView);
        mAlertViewExt.show();
    }

    @Override
    public void verifyNumber() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewGroup extView = (ViewGroup) LayoutInflater.from(PhoneActivity.this).inflate(R.layout.alertext_form, null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setHint("please input code");
                mAlertViewExt = new AlertView("提示", "短信已发出，请输入验证码", "取消", null, new String[]{"确定"}
                        , PhoneActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            mvpPresenter.verifyCode(etName.getText().toString());
                        }
                    }
                });
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        boolean isOpen = imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
            }
        }, 500);

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
        initNumber();
        toastShow("修改成功");
    }

    @Override
    public void verifyFail(String error) {
        toastShow(error);
    }

    @Override
    public void initNumber() {
        String number=BmobUser.getCurrentUser(User.class).getMobilePhoneNumber();
        if (!TextUtils.isEmpty(number)){
            tvNumber.setText(number.substring(0,3)+"****"+number.substring(7));
        }else {
            tvNumber.setText(R.string.no_bind);
        }
    }


}
