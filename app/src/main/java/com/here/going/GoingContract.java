package com.here.going;

import com.here.bean.ImActivity;
import com.here.bean.Join;
import com.here.bean.User;

import java.util.List;

/**
 * Created by hyc on 2017/7/5 17:30
 */

public interface GoingContract {

    void setGoingData(ImActivity imActivity);

    void showLoading();

    void stopLoading();

    void setSurplusTime(int surplus,int time);

    void loadPhoneNumber(String number);

    void loadJoinUserInfo(List<Join> joins,ImActivity imActivity);

    boolean isSelf();

    void fail(String error);

    void deleteSuccess();

    void confirmDelete();

    void contract(User user);

}
