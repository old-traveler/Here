package com.here.photo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.util.CommonUtils;
import com.scwang.smartrefresh.header.waveswipe.DropBounceInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;
import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoView;

import static com.autonavi.ae.gmap.glanimation.ADGLAnimation.DEFAULT_DURATION;


public class PhotoActivity extends MvpActivity<PhotoPresenter> implements PhotoContract {


    @Bind(R.id.photo)
    PhotoView photo;
    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        mvpPresenter.showImage();
    }



    @Override
    protected PhotoPresenter createPresenter() {
        return new PhotoPresenter();
    }


    @Override
    public void loadingImage(String url) {
        Glide.with(this)
                .load(url)
                .into(photo);

    }

    @Override
    public void loadingImage(int url) {
        Glide.with(this)
                .load(url)
                .into(photo);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }





}
