package com.here.imdetails;

import com.here.bean.ImActivity;

/**
 * Created by hyc on 2017/7/8 09:48
 */

public interface ImDetailsContract {

    ImActivity getImActivity();

    void loadingData(ImActivity imActivity);

    void showLoading();

    void stopLoading();

    void fail(String error);

    void sendApplySuccess();

    void joinSuccess();

    void joinFail(String error);

    void followSuccess();

    void hadFollowed();

}
