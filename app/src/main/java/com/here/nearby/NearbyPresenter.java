package com.here.nearby;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.here.HereApplication;
import com.here.base.BasePresenter;
import com.here.bean.ImActivity;
import com.here.bean.User;
import com.here.util.ImActivityUtil;
import com.here.util.NetworkState;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/6/21 14:51
 */

public class NearbyPresenter extends BasePresenter<NearbyContract> implements AMap.OnMyLocationChangeListener {

    private String kind;

    public static boolean going = false;

    private List<ImActivity> imActivities;

    private LatLng myLatLng;

    private boolean isLoading = false;

    private boolean isFirstLocation = true;

    private boolean isNeedLoadMap = false;

    public boolean isLoading() {
        return isLoading;
    }

    private boolean isNet = true;

    public void checkMyPublisher() {
        ImActivity imActivity = ImActivityUtil.getImActivityInfo(BmobUser.getCurrentUser(User.class));
        if (imActivity != null) {
            Calendar now = Calendar.getInstance();
            String[] time = imActivity.getOverTime().split("-");
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH);
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            if (Integer.parseInt(time[0]) == year && Integer.parseInt(time[1]) == month && Integer.parseInt(time[2]) == day) {
                if (hour < Integer.parseInt(time[3].split(":")[0]) || hour == Integer.parseInt(time[3].split(":")[0]) &&
                        minute <= Integer.parseInt(time[3].split(":")[1])) {
                    mvpView.isGoing(true);
                    going=true;
                } else {
                    going=false;
                    mvpView.isGoing(false);
                }

            } else {
                going=false;
                mvpView.isGoing(false);
            }
        } else {
            going=false;
            mvpView.isGoing(false);
        }

    }

    public void queryNearByImActivity(double latitude, double longitude) {
        if (!isLoading) {
            isLoading = true;
            ImActivityUtil.getNearByImActivity(latitude, longitude,kind,
                    new ImActivityUtil.OnGetNearByListener() {
                @Override
                public void success(List<ImActivity> activities) {
                    if (!isNet) {
                        mvpView.cancelLocation();
                        isNet = true;
                    }

                    if (activities.size() != 0) {
                        if (imActivities == null) {
                            imActivities = activities;
                            mvpView.loadingSuccess(activities);
                        } else {
                            if (imActivities.size() == activities.size()) {
                                for (int i = 0; i < imActivities.size(); i++) {
                                    if (!imActivities.get(i).getObjectId()
                                            .equals(activities.get(i).getObjectId())) {
                                        mvpView.loadingSuccess(activities);
                                        imActivities = activities;
                                        return;
                                    }
                                }
                                loadingComplete();
                            } else {
                                imActivities = activities;
                                mvpView.loadingSuccess(activities);
                            }
                        }
                    } else {
                        loadingComplete();
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null){
                        mvpView.loadingFail(error);
                        isLoading = false;
                    }

                }
            });
        }


    }

    public void queryNearByImActivity(final boolean isRefresh) {
        if (!isFirstLocation && isNeedLoadMap && NetworkState
                .networkConnected(HereApplication.getContext())) {
            isNeedLoadMap = false;
            mvpView.cancelLocation();
        }
        if (!isLoading) {
            if (myLatLng != null) {
                isLoading = true;
                ImActivityUtil.getNearByImActivity(myLatLng.latitude, myLatLng
                        .longitude,kind,new ImActivityUtil.OnGetNearByListener() {
                    @Override
                    public void success(List<ImActivity> activities) {
                        Log.i("TAG","类型是"+kind + activities.size());
                        if (activities.size() != 0) {
                            if (imActivities == null) {
                                imActivities = activities;
                                mvpView.loadingSuccess(activities);
                            } else {
                                if (imActivities.size() == activities.size() && isRefresh) {
                                    for (int i = 0; i < imActivities.size(); i++) {
                                        if (!imActivities.get(i).getObjectId()
                                                .equals(activities.get(i).getObjectId())) {
                                            mvpView.loadingSuccess(activities);
                                            imActivities = activities;
                                            return;
                                        }
                                    }
                                    loadingComplete();
                                } else {
                                    imActivities = activities;
                                    mvpView.loadingSuccess(activities);
                                }
                            }
                        } else {
                            Log.i("TAG","loadingComplete"+kind + activities.size());
                            loadingComplete();
                        }
                    }

                    @Override
                    public void fail(String error) {
                        if (mvpView != null){
                            isLoading = false;
                            mvpView.loadingFail(error);
                        }

                    }
                });
            }
        }

    }

    public void loadingComplete() {
        isLoading = false;
    }


    @Override
    public void onMyLocationChange(Location location) {
        if (isFirstLocation && !NetworkState.networkConnected(HereApplication.getContext())) {
            isFirstLocation = false;
            isNeedLoadMap = true;
        }
        if (location != null) {
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                if (myLatLng == null && NetworkState.networkConnected(HereApplication.getContext())) {
                    queryNearByImActivity(location.getLatitude(), location.getLongitude());
                    myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            } else {
                if (mvpView == null){
                    mvpView.loadingFail("定位失败");
                }

            }
        } else {
            if (mvpView == null){
                isNet = false;
                mvpView.loadingFail("定位失败");
            }

        }
    }


    public LatLng getMyLatLng() {
        return myLatLng;
    }



    public void setKind(String kind) {
        this.kind = kind;
    }
}
