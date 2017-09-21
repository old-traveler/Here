package com.here.feedback;

import com.here.base.BasePresenter;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/6/24 13:22
 */

public class FeedBackPresenter extends BasePresenter<FeedBackContract> {

    public void commitFeedback(){
        if (!mvpView.isSelectedType()){
            mvpView.showTips("请选择一个类型");
        }else if (!mvpView.isFillNumber()){
            mvpView.showTips("请填写联系电话");
        }else if (!mvpView.isFillFeedback()){
            mvpView.showTips("请填写反馈意见");
        }else {
            mvpView.showLoading();
            mvpView.getFeedback().save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null && mvpView != null){
                        mvpView.stopLoading();
                        mvpView.feedBackSuccess();
                    }else if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.showTips(e.getMessage());
                    }
                }
            });
        }
    }

}
