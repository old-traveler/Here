package com.here.personal;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.example.android.dialog.picker.DataPickerDialog;
import com.example.android.dialog.picker.DatePickerDialog;
import com.example.android.dialog.picker.RegionPickerDialog;
import com.here.HereApplication;
import com.here.R;
import com.here.adapter.PublishImageAdapter;
import com.here.adapter.ShowTipsAdapter;
import com.here.base.MvpActivity;
import com.here.bean.Tip;
import com.here.bean.User;
import com.here.email.EmailActivity;
import com.here.introduction.IntroductionActivity;
import com.here.nickname.NicknameActivity;
import com.here.other.CacheManager;
import com.here.photo.PhotoActivity;
import com.here.photo.PhotoPresenter;
import com.here.privacy.PrivacyActivity;
import com.here.tips.TipsActivity;
import com.here.view.MyGridLayoutManager;
import com.here.view.UnfoldAndZoomScrollView;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

public class PersonalActivity extends MvpActivity<PersonalPresenter> implements PersonalContract {

    @Bind(R.id.uz_personal)
    UnfoldAndZoomScrollView uzPersonal;
    @Bind(R.id.tb_personal)
    Toolbar tbPersonal;
    @Bind(R.id.el_tool_bar)
    RelativeLayout elToolBar;
    @Bind(R.id.tv_tool_title)
    TextView tvToolTitle;
    @Bind(R.id.iv_personal_bg)
    ImageView ivPersonalBg;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @Bind(R.id.iv_age)
    ImageView ivAge;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.rl_age)
    RelativeLayout rlAge;
    @Bind(R.id.iv_sex_tips)
    ImageView ivSexTips;
    @Bind(R.id.iv_sex)
    ImageView ivSex;
    @Bind(R.id.rl_sex)
    RelativeLayout rlSex;
    @Bind(R.id.iv_address)
    ImageView ivAddress;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.rl_address)
    RelativeLayout rlAddress;
    @Bind(R.id.rl_tips)
    RelativeLayout rlTips;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.rl_birthday)
    RelativeLayout rlBirthday;
    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;
    @Bind(R.id.rl_introduction)
    RelativeLayout rlIntroduction;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.rl_phone)
    RelativeLayout rlPhone;
    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.rl_email)
    RelativeLayout rlEmail;
    @Bind(R.id.iv_personal_head)
    CircleImageView ivPersonalHead;
    @Bind(R.id.rv_personal_tips)
    RecyclerView rvPersonalTips;

    private ShowTipsAdapter showTipsAdapter;
    public static final String AVATAR_FILE_NAME = "avatar.png";
    public static final int REQUEST_CODE_AVATAR = 100;
    public static final int REQUEST_CODE_IMAGE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_personal);
        initHome();
        initUserData();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back);
        elToolBar.getBackground().setAlpha(0);
        uzPersonal.setUpSlipListener(new UnfoldAndZoomScrollView.UpSlipListener() {
            @Override
            public void upSlipping(float distance) {
                elToolBar.getBackground().setAlpha((int) (distance * 255));
                if (distance > 0.5f) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.black_back);
                    menu.findItem(R.id.more).setIcon(R.drawable.info_more);
                    tvToolTitle.setTextColor(Color.BLACK);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back);
                    menu.findItem(R.id.more).setIcon(R.drawable.white_info_more);
                    tvToolTitle.setTextColor(Color.WHITE);
                }
            }
        });
        rvPersonalTips.setLayoutManager(new MyGridLayoutManager(this, 4));

        List<Tip> tips = new ArrayList<>();
        String[] tips_name = HereApplication.getContext().getResources().getStringArray(R.array.tip_name);
        String[] tips_slogan = HereApplication.getContext().getResources().getStringArray(R.array.tip_slogan);
        int[] bg = HereApplication.getContext().getResources().getIntArray(R.array.tips_bg);
        for (int i = 0; i < 24; i++) {
            Tip tip = new Tip();
            tip.setHave(false);
            tip.setColor(bg[i]);
            tip.setName(tips_name[i]);
            tip.setSlogan(tips_slogan[i]);
            tips.add(tip);
        }
        showTipsAdapter = new ShowTipsAdapter(tips);
        showTipsAdapter.setListener(new PublishImageAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                updateTips();
            }
        });
        rvPersonalTips.setAdapter(showTipsAdapter);
    }

    @Override
    protected void onResume() {
        mvpPresenter.updateInfo();
        super.onResume();
    }

    @Override
    public void initUserData() {
        User user = BmobUser.getCurrentUser(User.class);
        if (!TextUtils.isEmpty(user.getNickname())) {
            tvToolTitle.setText(user.getNickname());
            tvNickname.setText(user.getNickname());
        }
        tvAge.setText(user.getAge() + "岁");
        if (!TextUtils.isEmpty(user.getSex())) {
            if (user.getSex().equals("男")) {
                ivSex.setImageResource(R.drawable.man);
            } else {
                ivSex.setImageResource(R.drawable.woman);
            }
        }
        if (!TextUtils.isEmpty(user.getAddress())) {
            tvAddress.setText(user.getAddress());
        } else {
            tvAddress.setText(R.string.china);
        }
        if (!TextUtils.isEmpty(user.getDateOfBirth())) {
            tvBirthday.setText(user.getDateOfBirth());
        } else {
            tvBirthday.setText(R.string.no_birthday);
        }
        if (!TextUtils.isEmpty(user.getIntroduction())) {
            tvIntroduction.setText(user.getIntroduction());
        } else {
            tvIntroduction.setText(R.string.no_introduction);
        }
        if (!TextUtils.isEmpty(user.getMobilePhoneNumber())) {
            tvPhone.setText(user.getMobilePhoneNumber());
        } else {
            tvPhone.setText(R.string.no_bind);
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            tvEmail.setText(user.getEmail() + (user.getEmailVerified() ? " (已认证)" : " (未认证)"));
        } else {
            tvEmail.setText(R.string.no_bind);
        }

        if (!TextUtils.isEmpty(user.getHeadImageUrl())) {
            Glide.with(this)
                    .load(user.getHeadImageUrl())
                    .into(ivPersonalHead);
        } else {
            Glide.with(this)
                    .load(R.drawable.grils)
                    .into(ivPersonalHead);
        }

        if (!TextUtils.isEmpty(user.getBackgroundUrl())) {
            Glide.with(this)
                    .load(user.getBackgroundUrl())
                    .into(ivPersonalBg);
        } else {
            Glide.with(this)
                    .load(R.drawable.grils)
                    .into(ivPersonalBg);
        }


    }

    @Override
    public void startImagePicker() {
        SImagePicker
                .from(PersonalActivity.this)
                .pickMode(SImagePicker.MODE_AVATAR)
                .showCamera(true)
                .cropFilePath(
                        CacheManager.getInstance().getImageInnerCache()
                                .getAbsolutePath(AVATAR_FILE_NAME))
                .forResult(REQUEST_CODE_AVATAR);
    }

    @Override
    public void showTheBigHead() {
        PhotoPresenter.imageUrl = BmobUser.getCurrentUser(User.class).getHeadImageUrl();
        startActivity(new Intent(this, PhotoActivity.class));
    }

    @Override
    public void background() {
        new AlertView("背景图", null, "取消", new String[]{"更换背景"}, new String[]{"查看大图"}, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    updateBackground();
                } else if (position == 1) {
                    showTheBigBg();
                }
            }
        }).show();

    }

    @Override
    public void showTheBigBg() {
        PhotoPresenter.imageUrl = BmobUser.getCurrentUser(User.class).getBackgroundUrl();
        startActivity(new Intent(this, PhotoActivity.class));
    }

    public void choiceOfHead() {
        new AlertView("头像管理", null, "取消", new String[]{"更换头像"}, new String[]{"查看大图"}, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    startImagePicker();
                } else if (position == 1) {
                    showTheBigHead();
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AVATAR) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            mvpPresenter.updateHead(pathList.get(0));
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            mvpPresenter.updateBackground(pathList.get(0));
        }
    }


    private Menu menu;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more) {
            updatePrivacy();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected PersonalPresenter createPresenter() {
        return new PersonalPresenter();
    }

    @Override
    public void updateNickName() {
        startActivity(new Intent(this, NicknameActivity.class));
    }

    @Override
    public void updateAge() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("" + i);
        }
        DataPickerDialog dialog = builder.setUnit("岁").setData(data).setSelection(1).setTitle("年龄")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        mvpPresenter.updateAge(Integer.parseInt(itemValue));

                    }
                }).create();

        dialog.show();
    }

    @Override
    public void updateSex() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = new ArrayList<>();
        data.add("男");
        data.add("女");
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("性别")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        mvpPresenter.updateSex(itemValue);
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void updateAddress() {
        RegionPickerDialog.Builder builder = new RegionPickerDialog.Builder(this);

        RegionPickerDialog dialog = builder.setOnRegionSelectedListener(new RegionPickerDialog.OnRegionSelectedListener() {
            @Override
            public void onRegionSelected(String[] cityAndArea) {
                mvpPresenter.updateAddress(cityAndArea[0] + " " + cityAndArea[1]);
            }
        }).create();

        dialog.show();
    }

    @Override
    public void updateTips() {
        mActivity.startActivity(new Intent(this, TipsActivity.class));
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
    public void updateFail(String error) {
        toastShow(error);
    }

    @Override
    public void updateBirthday() {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        DatePickerDialog dialog = builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                mvpPresenter.updateBirthday(dates[0] + "-" + dates[1] + "-" + dates[2]);
            }
        }).create();
        dialog.show();
    }

    @Override
    public void updateIntroduction() {
        startActivity(new Intent(this, IntroductionActivity.class));
    }

    @Override
    public void updateEmail() {
        startActivity(new Intent(this, EmailActivity.class));
    }

    @Override
    public void updatePrivacy() {
        startActivity(new Intent(this, PrivacyActivity.class));
    }

    @Override
    public void showTheNumber() {

    }


    @Override
    public void updateBackground() {
        SImagePicker
                .from(PersonalActivity.this)
                .pickMode(SImagePicker.MODE_AVATAR)
                .showCamera(true)
                .cropFilePath(
                        CacheManager.getInstance().getImageInnerCache()
                                .getAbsolutePath(AVATAR_FILE_NAME))
                .forResult(REQUEST_CODE_IMAGE);

    }

    @OnClick({R.id.iv_personal_bg, R.id.rl_nickname, R.id.rl_age, R.id.rl_sex, R.id.rl_address, R.id.rl_tips, R.id.rl_birthday, R.id.rl_introduction, R.id.rl_phone, R.id.rl_email, R.id.iv_personal_head,R.id.rv_personal_tips})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_personal_bg:
                background();
                break;
            case R.id.rl_nickname:
                updateNickName();
                break;
            case R.id.rl_age:
                updateAge();
                break;
            case R.id.rl_sex:
                updateSex();
                break;
            case R.id.rl_address:
                updateAddress();
                break;
            case R.id.rl_tips:
                updateTips();
                break;
            case R.id.rl_birthday:
                updateBirthday();
                break;
            case R.id.rl_introduction:
                updateIntroduction();
                break;
            case R.id.rl_phone:
                showTheNumber();
                break;
            case R.id.rl_email:
                updateEmail();
                break;
            case R.id.iv_personal_head:
                choiceOfHead();
                break;
            case R.id.rv_personal_tips:
                updateTips();
                break;
        }
    }

}
