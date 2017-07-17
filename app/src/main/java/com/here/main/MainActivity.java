package com.here.main;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.FragmentAdapter;
import com.here.base.MvpActivity;
import com.here.bean.User;
import com.here.chat.ChatActivity;
import com.here.community.CommunityFragment;
import com.here.feedback.FeedBackActivity;
import com.here.follow.FollowFragment;
import com.here.follow.info.FollowActivity;
import com.here.message.MessageActivity;
import com.here.nearby.NearbyFragment;
import com.here.personal.PersonalActivity;
import com.here.receiver.ConnectionChangeReceiver;
import com.here.scan.ScanActivity;
import com.here.setting.SettingActivity;
import com.here.util.ImActivityUtil;
import com.here.util.ImUtil;
import com.here.util.JoinUtil;
import com.here.view.MyViewPage;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;


public class MainActivity extends MvpActivity<MainPresenter> implements MainContract {


    @Bind(R.id.tv_tool_main)
    TextView tvToolMain;
    @Bind(R.id.tb_main)
    Toolbar tbMain;
    @Bind(R.id.vp_main)
    MyViewPage vpMain;
    @Bind(R.id.dl_main)
    DrawerLayout dlMain;
    @Bind(R.id.rv_enter_scan)
    RelativeLayout rvScan;
    @Bind(R.id.iv_side_background)
    ImageView ivSideBackground;
    @Bind(R.id.cv_side_head)
    CircleImageView cvSideHead;
    @Bind(R.id.rv_my_activity)
    RelativeLayout rvMyActivity;
    @Bind(R.id.rv_my_follow)
    RelativeLayout rvMyFollow;
    @Bind(R.id.rv_my_grade)
    RelativeLayout rvMyGrade;
    @Bind(R.id.rv_feedback)
    RelativeLayout rvFeedback;
    @Bind(R.id.rv_setting)
    RelativeLayout rvSetting;
    @Bind(R.id.ll_side)
    LinearLayout llSide;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_side_nickname)
    TextView tvSideNickname;
    @Bind(R.id.tv_side_introduction)
    TextView tvSideIntroduction;

    private FragmentAdapter adapter;

    private ConnectionChangeReceiver receiver;
    /**
     * 记录ViewPage当前的选中界面
     */
    private int currentItem = 0;

    private long mkeyTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_main);
        initToolBarText(R.id.tv_tool_main, getString(R.string.toolbar_title_main), R.drawable.category);
        initViewPage();
        ImUtil.connectServer();
        addActivity(this);
        getLocationPre();
        initUserData();
        Connector.getDatabase();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 1500) {
                mkeyTime = System.currentTimeMillis();
                toastShow("再按一次退出程序");
            } else {
                finish();
                System.exit(0);
            }
            return false;

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void initUserData() {
        User user= BmobUser.getCurrentUser(User.class);
        if (!TextUtils.isEmpty(user.getNickname())){
            tvSideNickname.setText(user.getNickname());
        }

        if (!TextUtils.isEmpty(user.getIntroduction())){
            tvSideIntroduction.setText(user.getIntroduction());
        }

        if (!TextUtils.isEmpty(user.getHeadImageUrl())){
            Glide.with(this)
                    .load(user.getHeadImageUrl())
                    .into(cvSideHead);
        }
        if (!TextUtils.isEmpty(user.getBackgroundUrl())){
            Glide.with(this)
                    .load(user.getBackgroundUrl())
                    .into(ivSideBackground);
        }

    }




    @Override
    public void registerReceiver() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver=new ConnectionChangeReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver() {
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        llSide.setBackgroundColor(Color.WHITE);
        mvpPresenter.updateUserInfo();
        super.onResume();
    }



    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new NearbyFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new FollowFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(adapter);

        vpMain.setRightDistance(getWindowManager().getDefaultDisplay().getWidth());

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (currentItem - 1 == position) {
                    tvToolMain.setAlpha(positionOffset);
                } else {
                    tvToolMain.setAlpha(1 - positionOffset);
                }

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvToolMain.setText(getString(R.string.toolbar_title_main));
                        currentItem = 0;
                        break;
                    case 1:
                        tvToolMain.setText(getString(R.string.toolbar_title_community));
                        currentItem = 1;
                        break;
                    case 2:
                        tvToolMain.setText(getString(R.string.toolbar_title_follow));
                        currentItem = 2;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dlMain.openDrawer(GravityCompat.START);
                return true;
            case R.id.message:
//                BmobIMUserInfo info =new BmobIMUserInfo();
//                info.setAvatar("http://bmob-cdn-12662.b0.upaiyun.com/2017/07/04/cd40eb8c0ce741eab6604ed692d14e7a.jpg");
//                info.setUserId("233cbba0c8");
//                info.setName("周滔");
//                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
//                    @Override
//                    public void done(BmobIMConversation c, BmobException e) {
//                        if(e==null){
//                            //在此跳转到聊天页面
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("c", c);
//                            startActivity(ChatActivity.class, bundle, true);
//                        }else{
//                            toastShow(e.getMessage()+"("+e.getErrorCode()+")");
//                        }
//                    }
//                });
                startActivity(new Intent(this, MessageActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @OnClick({R.id.iv_side_background, R.id.cv_side_head, R.id.rv_enter_scan, R.id.rv_my_activity, R.id.rv_my_follow, R.id.rv_my_grade, R.id.rv_feedback, R.id.iv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_side_background:
                enterPersonal();
                break;
            case R.id.cv_side_head:
                enterPersonal();
                break;
            case R.id.rv_enter_scan:
                enterScan();
                break;
            case R.id.rv_my_activity:
                checkActivity();
                break;
            case R.id.rv_my_follow:
                checkFollow();
                break;
            case R.id.rv_my_grade:
                checkGrade();
                break;
            case R.id.rv_feedback:
                feedback();
                break;
            case R.id.iv_setting:
                enterSetting();
                break;
        }
    }

    @Override
    public void enterScan() {
        startActivity(new Intent(this, ScanActivity.class));
    }

    @Override
    public void checkActivity() {

    }

    @Override
    public void checkFollow() {
        startActivity(new Intent(this, FollowActivity.class));
    }

    @Override
    public void checkGrade() {

    }

    @Override
    public void feedback() {
        startActivity(new Intent(this, FeedBackActivity.class));
    }

    @Override
    public void enterSetting() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void enterPersonal() {
        startActivity(new Intent(this, PersonalActivity.class));
    }


}
