package com.here.photo;

import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.here.R;
import com.here.base.MvpActivity;
import com.here.util.DensityUtil;
import com.here.view.ViewPagerFix;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;


public class PhotoActivity extends MvpActivity<PhotoPresenter> implements PhotoContract {


    @Bind(R.id.vp_photo)
    ViewPagerFix vpPhoto;
    private List<PhotoView> vpLists;
    private List<String> imagesUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mvpPresenter.attachView(this);
        getImages();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        loadImages(imagesUrl);
        initView();

    }


    private void initView() {
        vpLists.get(getPosition()).setTransitionName("image");
        vpPhoto.setAdapter(new ViewPagerAdapter());
        vpPhoto.setCurrentItem(getPosition());
    }


    @Override
    protected PhotoPresenter createPresenter() {
        return new PhotoPresenter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.vp_photo)
    public void onViewClicked() {

    }

    @Override
    public void getImages() {
        imagesUrl = getIntent().getStringArrayListExtra("images");
    }

    @Override
    public void loadImages(List<String> images) {
        vpLists = new ArrayList<>();
        for (int i = 0; i < imagesUrl.size(); i++) {
            PhotoView photoView = new PhotoView(this);
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    new Thread(){
                        public void run() {
                            try{
                                Instrumentation inst = new Instrumentation();
                                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                            }
                            catch (Exception e) {
                                Log.e("Exception when onBack", e.toString());
                            }
                        }
                    }.start();
                }
            });
            Glide.with(this).load(imagesUrl.get(i))
                    .dontAnimate().into(photoView);
            vpLists.add(photoView);



        }
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra("position", 0);
    }

    public class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imagesUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView iv = vpLists.get(position);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
