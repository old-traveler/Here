package com.here.publish.appointment;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.here.base.BasePresenter;
import com.here.bean.Appointment;
import com.here.bean.User;
import com.here.util.FileUtil;
import com.here.util.TinyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/7/14 12:09
 */

public class AppointmentPresenter extends BasePresenter<AppointmentContract>  implements AMapLocationListener{



    public void publish(){
        Appointment appointment = mvpView.getAppointment();
        if (TextUtils.isEmpty(appointment.getAddress())){
            mvpView.fail("请先获取当前位置信息");
            return;
        }else if (TextUtils.isEmpty(appointment.getTitle())){
            mvpView.fail("标题不能为空");
            return;
        }else if (TextUtils.isEmpty(appointment.getDescribe())){
            mvpView.fail("活动描述不能为空");
            return;
        }else if (TextUtils.isEmpty(appointment.getStartDate())){
            mvpView.fail("活动开始时间不能为空");
            return;
        }else if (TextUtils.isEmpty(appointment.getOverDate())){
            mvpView.fail("活动结束时间不能为空");
            return;
        }else if (appointment.getJoinNumber() == 0){
            mvpView.fail("参加人数不能为空");
            return;
        }else if(TextUtils.isEmpty(appointment.getKind())){
            mvpView.fail("活动类型不能为空");
            return;
        }
        mvpView.showLoading();
        List<String> img = mvpView.getImages();
        if (img.size() > 0){
            String[] images = new String[img.size()];
            for (int i = 0; i < img.size(); i++) {
                images[i] = img.get(i);
            }
            TinyUtil.batchCompress(images, new TinyUtil.OnBatchCompressListener() {
                @Override
                public void success(String[] out) {
                    uploadImages(out);
                }

                @Override
                public void fail() {
                    mvpView.stopLoading();
                    mvpView.publishFail("压缩图片失败");
                }
            });
        }else {
            uploadAppointment(mvpView.getAppointment());
        }
    }

    public void uploadImages(String[] out){
        FileUtil.uploadBatch(out, new FileUtil.OnUploadBatchListener() {
            @Override
            public void success(List<String> images) {
                String[] image = new String[images.size()];
                for (int i = 0; i < images.size(); i++) {
                    image[i] = images.get(i);
                }
                mvpView.getAppointment().setImages(image);
                uploadAppointment(mvpView.getAppointment());
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.publishFail(error);
            }
        });
    }

    public void uploadAppointment(Appointment appointment){
        appointment.setPublisher(BmobUser.getCurrentUser(User.class));
        appointment.setPublishTime(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        appointment.setPublishDate(format.format(new Date(System.currentTimeMillis())));
        appointment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    mvpView.stopLoading();
                    mvpView.publishSuccess();
                }else {
                    mvpView.stopLoading();
                    if (e.getErrorCode() == 9016){
                        mvpView.publishFail("网络不给力");
                    }else {
                        mvpView.publishFail(e.getErrorCode()+e.getMessage());
                    }
                }
            }
        });
    }

    public void location(){
        mvpView.showLoading();
        mvpView.getLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0){
            mvpView.setLocationMessage(aMapLocation.getDistrict()+" "+aMapLocation
                    .getStreet()+" "+aMapLocation.getPoiName()+"附近");
            mvpView.stopLoading();
            mvpView.getAppointment().setAddress(aMapLocation.getDistrict()+" "+aMapLocation
                    .getStreet()+" "+aMapLocation.getPoiName()+"附近");
            mvpView.getAppointment().setLatitude(aMapLocation.getLatitude());
            mvpView.getAppointment().setLongitude(aMapLocation.getLongitude());
        }else {
            mvpView.stopLoading();
            mvpView.getLocationFail();
        }
    }
}
