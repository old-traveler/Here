package com.here.photo;

import android.text.TextUtils;

import com.here.R;
import com.here.base.BasePresenter;

/**
 * Created by hyc on 2017/7/3 05:58
 */

public class PhotoPresenter extends BasePresenter<PhotoContract> {

    public static String imageUrl;

    public void showImage(){
        if (!TextUtils.isEmpty(imageUrl)){
            mvpView.loadingImage(imageUrl);
        }else{
            mvpView.loadingImage(R.drawable.grils);
        }
    }

}
