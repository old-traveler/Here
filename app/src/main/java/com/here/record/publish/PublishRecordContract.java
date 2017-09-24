package com.here.record.publish;

import com.here.bean.Community;
import com.here.bean.ImActivity;
import com.here.bean.User;

import java.util.List;

/**
 * Created by hyc on 2017/9/24 11:32
 */

public interface PublishRecordContract {

    void setShareRecord(List<Community> communities);

    void addShareRecord(List<Community> communities);

    void setAppointmentRecord(List<Community> communities);

    void addAppointmentRecord(List<Community> communities);

    void setImActivityRecord(List<ImActivity> imActivities);

    void addImActivityRecord(List<ImActivity> imActivities);

    void loadFail(String error);

    User getPublisher();

}
