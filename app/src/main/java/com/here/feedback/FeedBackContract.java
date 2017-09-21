package com.here.feedback;

import com.here.bean.FeedBack;

/**
 * Created by hyc on 2017/6/24 13:22
 */

public interface FeedBackContract {

    FeedBack getFeedback();

    void showTips(String tips);

    boolean isSelectedType();

    boolean isFillFeedback();

    boolean isFillNumber();

    void showLoading();

    void stopLoading();

    void feedBackSuccess();




}
