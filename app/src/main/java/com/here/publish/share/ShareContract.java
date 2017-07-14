package com.here.publish.share;

import com.here.bean.Mood;

import java.util.List;

/**
 * Created by hyc on 2017/7/14 12:08
 */

public interface ShareContract {

    void showLoading();

    void stopLoading();

    void selectPic();

    void selectKind();

    Mood getMoodInfo();

    List<String> getImages();

    void fail(String error);

    void publishSuccess();

    void publishFail(String error);

}
