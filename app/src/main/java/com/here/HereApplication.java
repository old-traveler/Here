package com.here;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import com.here.receiver.HereMessageHandler;
import com.here.other.GlideImageLoader;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.zxy.tiny.Tiny;

import org.litepal.LitePal;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by hyc on 2017/6/23 14:11
 */

public class HereApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "e8c7f8ed34bb48dd4c5ab6b54d8cf11d");
        context=getApplicationContext();
        LitePal.initialize(this);
        Tiny.getInstance().init(this);
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new GlideImageLoader())
                .setToolbaseColor(Color.parseColor("#108de8"))
                .build());
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new HereMessageHandler());
    }
}
