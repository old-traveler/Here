package com.here.immediate;

import com.here.adapter.PublishImageAdapter;
import com.here.bean.ImActivity;

import java.util.List;

/**
 * Created by hyc on 2017/7/3 22:55
 */

public interface NewImmediateContract {
    ImActivity imActivity = new ImActivity();

    void initRecycler(List<String> images);

    void choicePic();

    void setListener(PublishImageAdapter.OnItemClickListener listener);

    void selectOverTime();

    int getActivityNumber();

    void getLocation();

    void selectKind();

    ImActivity getImActivity();

    void setLocationMessage(String Location);

    void getLocationFail();

    void showLoading();

    void stopLoading();

    void noFilled(String msg);

    void uploadSuccess();

}
