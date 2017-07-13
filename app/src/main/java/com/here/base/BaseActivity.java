package com.here.base;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.here.R;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;

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
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("加载中");
            progressDialog.show();
        }
        progressDialog.show();
    }

    public void dissmiss(){
        progressDialog.dismiss();
    }



}
