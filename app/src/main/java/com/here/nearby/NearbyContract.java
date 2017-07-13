package com.here.nearby;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.here.bean.ImActivity;

import java.util.List;

/**
 * Created by hyc on 2017/6/21 14:51
 */

public interface NearbyContract {



    void isGoing(boolean isGoing);

    void showLoading();

    void stopLoading();

    void loadingSuccess(List<ImActivity> imActivities);

    void loadingFail(String error);

    void downTheDetail();

    void upTheDetail();

    void loadActivityDetail(ImActivity imActivity);


    void cancelLocation();


}
