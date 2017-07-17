package com.here.record;

import com.here.bean.ImActivity;

import java.util.List;

/**
 * Created by hyc on 2017/7/17 18:49
 */

public interface RecordContract {

    void showLoading();

    void stopLoading();

    void loadFail(String error);

    void LoadActivity(List<ImActivity> imActivities);

}
