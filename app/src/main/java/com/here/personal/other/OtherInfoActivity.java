package com.here.personal.other;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.HereApplication;
import com.here.R;
import com.here.adapter.ShowTipsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Tip;
import com.here.bean.User;
import com.here.view.MyGridLayoutManager;
import com.here.view.UnfoldAndZoomScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherInfoActivity extends MvpActivity<OtherInfoPresenter> implements OtherInfoContract {

    @Bind(R.id.iv_other_info_bg)
    ImageView ivOtherInfoBg;
    @Bind(R.id.tv_other_nickname)
    TextView tvOtherNickname;
    @Bind(R.id.iv_other_age)
    ImageView ivOtherAge;
    @Bind(R.id.tv_other_age)
    TextView tvOtherAge;
    @Bind(R.id.iv_other_sex_tips)
    ImageView ivOtherSexTips;
    @Bind(R.id.iv_other_sex)
    ImageView ivOtherSex;
    @Bind(R.id.iv_other_address)
    ImageView ivOtherAddress;
    @Bind(R.id.tv_other_address)
    TextView tvOtherAddress;
    @Bind(R.id.rv_other_tips)
    RecyclerView rvOtherTips;
    @Bind(R.id.tv_other_birthday)
    TextView tvOtherBirthday;
    @Bind(R.id.tv_other_introduction)
    TextView tvOtherIntroduction;
    @Bind(R.id.tv_other_phone)
    TextView tvOtherPhone;
    @Bind(R.id.tv_other_email)
    TextView tvOtherEmail;
    @Bind(R.id.iv_other_head)
    CircleImageView ivOtherHead;
    @Bind(R.id.uz_other_info)
    UnfoldAndZoomScrollView uzOtherInfo;
    @Bind(R.id.rl_tool_bar)
    RelativeLayout rlToolBar;
    @Bind(R.id.tv_other_title)
    TextView tvOtherTitle;
    @Bind(R.id.btn_contract_other)
    Button btnContractOther;
    private ShowTipsAdapter showTipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);
        mvpPresenter.attachView(this);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_other_personal);
        initHome();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back);
        rlToolBar.getBackground().setAlpha(0);
        uzOtherInfo.setUpSlipListener(new UnfoldAndZoomScrollView.UpSlipListener() {
            @Override
            public void upSlipping(float distance) {
                rlToolBar.getBackground().setAlpha((int) (distance * 255));
                if (distance > 0.5f) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.black_back);
                    tvOtherTitle.setTextColor(Color.BLACK);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back);
                    tvOtherTitle.setTextColor(Color.WHITE);
                }
            }
        });
        rvOtherTips.setLayoutManager(new MyGridLayoutManager(this, 4));
        mvpPresenter.load();
    }

    @Override
    protected OtherInfoPresenter createPresenter() {
        return new OtherInfoPresenter();
    }

    @Override
    public User getUserInfo() {
        return (User) getIntent().getSerializableExtra("other");
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
    public void fail(String error) {
        toastShow(error);
    }

    @Override
    public void setUserInfo(User user) {
        if (TextUtils.isEmpty(user.getHeadImageUrl())) {
            Glide.with(this)
                    .load(R.drawable.grils)
                    .into(ivOtherHead);
        } else {
            Glide.with(this)
                    .load(user.getHeadImageUrl())
                    .into(ivOtherHead);
        }

        if (TextUtils.isEmpty(user.getBackgroundUrl())) {
            Glide.with(this)
                    .load(R.drawable.grils)
                    .into(ivOtherInfoBg);
        } else {
            Glide.with(this)
                    .load(user.getBackgroundUrl())
                    .into(ivOtherInfoBg);
        }

        tvOtherNickname.setText(user.getNickname());
        tvOtherAge.setText(user.getAge() + "岁");
        if (user.getSex() != null && user.getSex().equals("男")) {
            ivOtherSex.setImageResource(R.drawable.man);
        } else if (user.getSex() != null){
            ivOtherSex.setImageResource(R.drawable.woman);
        }

        tvOtherAddress.setText(user.getAddress());
        tvOtherBirthday.setText(user.getDateOfBirth());
        tvOtherIntroduction.setText(user.getIntroduction());
        if (!TextUtils.isEmpty(user.getMobilePhoneNumber())) {
            tvOtherPhone.setText(user.getMobilePhoneNumber().substring(0, 3) + "****" + user.getMobilePhoneNumber().substring(7));
        }
        if (!TextUtils.isEmpty(user.getEmail())){
            tvOtherEmail.setText(user.getEmail());
        }
        List<Tip> tips = new ArrayList<>();
        String[] tips_slogan = HereApplication.getContext().getResources().getStringArray(R.array.tip_slogan);
        int[] bg = HereApplication.getContext().getResources().getIntArray(R.array.tips_bg);
        if (user.getTips() != null) {
            for (int i = 0; i < user.getTips().length; i++) {
                Tip tip = new Tip();
                tip.setHave(true);
                tip.setColor(bg[i]);
                tip.setName(user.getTips()[i]);
                tip.setSlogan(tips_slogan[i]);
                tips.add(tip);
            }
        }
        showTipsAdapter = new ShowTipsAdapter(tips);
        rvOtherTips.setAdapter(showTipsAdapter);

    }


    @OnClick(R.id.btn_contract_other)
    public void onViewClicked() {
        openNewConversation(getUserInfo());
        finish();
    }
}
