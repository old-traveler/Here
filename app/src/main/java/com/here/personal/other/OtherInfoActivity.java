package com.here.personal.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.here.HereApplication;
import com.here.R;
import com.here.adapter.ShowTipsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Follow;
import com.here.bean.Tip;
import com.here.bean.User;
import com.here.personal.accusation.AccusationActivity;
import com.here.record.publish.PublishRecordActivity;
import com.here.util.CommonUtils;
import com.here.util.FollowUtil;
import com.here.view.MyGridLayoutManager;
import com.here.view.UnfoldAndZoomScrollView;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

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
    private Menu menu;
    private boolean hasFollow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this,false);
        setContentView(R.layout.activity_other_info);
        mvpPresenter.attachView(this);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_other_personal);
        initHome();
        getWindow().setEnterTransition(new Fade().setDuration(500));
        getWindow().setExitTransition(new Fade().setDuration(500));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back);
        rlToolBar.getBackground().setAlpha(0);
        uzOtherInfo.setUpSlipListener(new UnfoldAndZoomScrollView.UpSlipListener() {
            @Override
            public void upSlipping(float distance) {
                rlToolBar.getBackground().setAlpha((int) (distance * 255));
                if (distance > 0.5f) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.black_back);
                    menu.findItem(R.id.more).setIcon(R.drawable.info_more);
                    tvOtherTitle.setTextColor(Color.BLACK);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back);
                    menu.findItem(R.id.more).setIcon(R.drawable.white_info_more);
                    tvOtherTitle.setTextColor(Color.WHITE);
                }
            }
        });
        rvOtherTips.setLayoutManager(new MyGridLayoutManager(this, 4));
        mvpPresenter.load();
        CommonUtils.flymeSetStatusBarLightMode(getWindow(),true);
        ivOtherHead.setTransitionName("image");
        hasFollow = FollowUtil.isFollow(BmobUser.getCurrentUser().getObjectId(),getUserInfo().getObjectId());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more) {
            selectMore();
            return true;
        }else if (item.getItemId() == android.R.id.home){
            rlToolBar.getBackground().setAlpha(255);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.black_back);
            menu.findItem(R.id.more).setIcon(R.drawable.info_more);
            tvOtherTitle.setTextColor(Color.BLACK);
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            rlToolBar.getBackground().setAlpha(255);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.black_back);
            menu.findItem(R.id.more).setIcon(R.drawable.info_more);
            tvOtherTitle.setTextColor(Color.BLACK);
            super.onBackPressed();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


    private void selectMore() {
        if (hasFollow){
            new AlertView("更多操作", null, "取消", new String[]{"加入黑名单","不再关注"}
                    ,null, this, AlertView
                    .Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        mvpPresenter.joinBlackList();
                    } else if (position == 1) {
                        mvpPresenter.cancelFollow();
                    }
                }
            }).show();
        }else {
            new AlertView("更多操作", null, "取消", new String[]{"加入黑名单"}
                    , new String[]{"关注"}, this, AlertView
                    .Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        mvpPresenter.joinBlackList();
                    } else if (position == 1) {
                        mvpPresenter.followUser();
                    }
                }
            }).show();
        }

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
                    .load(R.drawable.head_info)
                    .into(ivOtherHead);
        } else {
            Glide.with(this)
                    .load(user.getHeadImageUrl())
                    .into(ivOtherHead);
        }

        if (TextUtils.isEmpty(user.getBackgroundUrl())) {
            Glide.with(this)
                    .load(R.drawable.info_bg)
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
        } else if (user.getSex() != null) {
            ivOtherSex.setImageResource(R.drawable.woman);
        }

        if (!TextUtils.isEmpty(user.getAddress())){
            tvOtherAddress.setText(user.getAddress());
        }else {
            tvOtherAddress.setText("未填写");
        }

        if (!TextUtils.isEmpty(user.getDateOfBirth())){
            tvOtherBirthday.setText(user.getDateOfBirth());
        }else {
            tvOtherBirthday.setText("未填写");
        }

        if(!TextUtils.isEmpty(user.getIntroduction())){
            tvOtherIntroduction.setText(user.getIntroduction());
        }else {
            tvOtherIntroduction.setText("这个人很懒，什么也没留下。");
        }



        if (!TextUtils.isEmpty(user.getMobilePhoneNumber())) {
            tvOtherPhone.setText(user.getMobilePhoneNumber()
                    .substring(0, 3) + "****" + user
                    .getMobilePhoneNumber().substring(7));
        }else {
            tvOtherPhone.setText("未绑定");
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            tvOtherEmail.setText(user.getEmail());
        }else {
            tvOtherEmail.setText("未绑定");
        }
        List<Tip> tips = new ArrayList<>();
        String[] tips_slogan = HereApplication.getContext()
                .getResources().getStringArray(R.array.tip_slogan);
        int[] bg = HereApplication.getContext()
                .getResources().getIntArray(R.array.tips_bg);
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

    @Override
    public void followSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertView("提示", "关注成功", "确定", null, null
                        ,OtherInfoActivity.this, AlertView.Style.Alert,null).show();
            }
        }, 500);
        hasFollow = true;
    }

    @Override
    public void blacklistSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertView("提示", "已加入黑名单", "确定", null,null
                        ,OtherInfoActivity.this, AlertView.Style.Alert, null).show();
            }
        }, 500);

    }

    @Override
    public void cancelFollowSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertView("提示", "取消关注成功", "确定", null,null
                        ,OtherInfoActivity.this, AlertView.Style.Alert, null).show();
            }
        }, 500);
        hasFollow = false;
    }


    @OnClick({R.id.btn_contract_other, R.id.rl_publisher_cord,R.id.btn_accusation_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_contract_other:
                openNewConversation(getUserInfo());
                finish();
                break;
            case R.id.rl_publisher_cord:
                Intent intent = new Intent(this
                        , PublishRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("publisher", getUserInfo());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_accusation_other:
                Intent intent1 = new Intent(OtherInfoActivity
                        .this, AccusationActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user", getUserInfo());
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
        }
    }


}
