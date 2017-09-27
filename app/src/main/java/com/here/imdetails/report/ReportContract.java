package com.here.imdetails.report;

import com.here.bean.ImActivity;

/**
 * Created by hyc on 2017/9/27 21:30
 */

public interface ReportContract {

    void showLoading();

    void stopLoading();

    void submitSuccess();

    void showTips(String error);

    ImActivity getImActivity();
}
