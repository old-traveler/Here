package com.here.follow.join;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dingmouren.paletteimageview.PaletteImageView;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.util.FindUtil;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinFindActivity extends MvpActivity<JoinFindPresenter> implements JoinFindContract {

    @Bind(R.id.palette)
    PaletteImageView palette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_find);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        setToolBar(R.id.tb_join_find);
        initHome();
        String url = FindUtil.getRecordUrlCache();
        if (!TextUtils.isEmpty(url) && !url.equals("refuse") && !url.equals("no")){
            loadImage(url);
        }else if (TextUtils.isEmpty(url)){
            mvpPresenter.queryIsJoin();
        }else {
            loadImage(R.drawable.add_image);
        }
    }

    @Override
    protected JoinFindPresenter createPresenter() {
        return new JoinFindPresenter();
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
    public void uploadImageSuccess(String path) {
        new AlertView("提示", "上传成功", "确定", null, null
                , this, AlertView.Style.Alert, null).show();
        loadImage(path);
    }

    @Override
    public void showTip(String msg) {
        toastShow(msg);
    }

    @Override
    public void selectImage() {
        if (getCcamra()) {
            SImagePicker
                    .from(this)
                    .maxCount(1)
                    .rowCount(3)
                    .showCamera(true)
                    .pickMode(SImagePicker.MODE_IMAGE)
                    .forResult(101);
        } else {
            Toast.makeText(mActivity, "请给与读取本地相册权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void hadJoin() {
        loadImage(FindUtil.getRecordUrlCache());
    }

    @Override
    public void hadNoJoin() {
        loadImage(R.drawable.add_image);
    }

    private void loadImage(String path){
        Glide.with(this).load(path).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource
                    , GlideAnimation<? super Bitmap> glideAnimation) {
                palette.setBitmap(resource);
            }
        });
    }

    private void loadImage(int path){
        Glide.with(this).load(path).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource
                            , GlideAnimation<? super Bitmap> glideAnimation) {
                        palette.setBitmap(resource);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            if (pathList.size() == 1) {
                mvpPresenter.upload(pathList.get(0));
            }
        }
    }

    @OnClick(R.id.palette)
    public void onViewClicked() {
        selectImage();
    }
}
