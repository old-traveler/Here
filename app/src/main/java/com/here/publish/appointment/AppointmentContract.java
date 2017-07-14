package com.here.publish.appointment;

import com.here.bean.Appointment;

import java.util.List;

/**
 * Created by hyc on 2017/7/14 12:09
 */

public interface AppointmentContract {

    void showLoading();

    void stopLoading();

    void selectPic();

    void selectKind();

    void setLocationMessage(String location);

    void getLocation();

    List<String> getImages();

    void fail(String error);

    void publishSuccess();

    Appointment getAppointment();

    void getLocationFail();

    void publishFail(String error);

}
