package com.here.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.here.R;
import com.here.bean.User;
import com.here.chat.ChatActivity;
import com.here.main.MainActivity;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/6/21 13:43
 */

public class BaseActivity extends AppCompatActivity{
    /**
     * 管理所有的activity资源对象
     */
    private static List<BaseActivity> activities;
    /**
     * toolbar
     */
    public Toolbar mToolbar;

    public Activity mActivity;

    private Dialog progressDialog;

    private static final String PACKAGE_URL_SCHEME = "package:";//权限方案

    public static final int PERMISSION_DENIEG = 1;//权限不足，权限被拒绝的时候

    private static final int REQUEST_CODE_PICK_IMAGE = 3;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        mActivity = this;

    }

    public void setToolBar(int toolId){
        Toolbar toolbar = (Toolbar) findViewById(toolId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            this.mToolbar=toolbar;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void initToolBarAsHome(String title) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(title);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.black_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    public void initHome(){
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.black_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    public void initToolBarText(int tvId,String title,int res) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            TextView toolbarTitle = (TextView) mToolbar.findViewById(tvId);
            toolbarTitle.setText(title);

        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(res);
        }
    }


    public void initToolBarText(int tvId,String title) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            TextView toolbarTitle = (TextView) mToolbar.findViewById(tvId);
            toolbarTitle.setText(title);

        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void log(String text){
        Log.i("here------",text);
    }

    public void addActivity(BaseActivity baseActivity){
        if (activities==null){
            activities=new ArrayList<>();
        }
        activities.add(baseActivity);
    }

    public void finishAll(){
        for (BaseActivity activity:activities){
            if (activity!=null){
                activity.finish();
            }
        }
    }

    public void finishPreAll(){
        for (int i = 0; i < activities.size()-2; i++) {
            activities.get(i).finish();
        }
    }

    public void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new Dialog(this,R.style.progress_dialog);
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("加载中");
            progressDialog.show();
        }
        progressDialog.show();
    }

    public void dissmiss(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public boolean getCcamra(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA )
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            return true;
        }
    }
    //申请语音权限
    public boolean getVoice(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS
                    ,Manifest.permission.CAPTURE_AUDIO_OUTPUT,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return false;
        }else{
            return true;
        }
    }
    public void getContracts(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            return;
        }
    }

    public boolean getStorage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            return true;
        }
    }


    public void getRecord(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            return;
        }
    }

    public boolean getLocationPre(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }else{
            return true;
        }

    }

    public void getPhone(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        } else {
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showMissingPermissionDialog(requestCode, grantResults);
    }

    private void showMissingPermissionDialog(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("帮助");//提示帮助
                builder.setMessage(R.string.string_help_text);

                //如果是拒绝授权，返回
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(PERMISSION_DENIEG);//权限不足
                        Toast.makeText(mActivity, "取消", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转系统应用权限
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                        startActivity(intent);
                        return;
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }


    public void openNewConversation(User user){
        BmobIMUserInfo info =new BmobIMUserInfo();
        info.setAvatar(user.getHeadImageUrl());
        info.setUserId(user.getObjectId());
        info.setName(user.getNickname());
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if(e==null){
                    //在此跳转到聊天页面
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class,bundle,false);
                }else{
                    toastShow(e.getMessage()+"("+e.getErrorCode()+")");
                }
            }
        });
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




}
