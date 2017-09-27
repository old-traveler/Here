package com.here.personal.accusation;

import android.text.TextUtils;

import com.here.base.BasePresenter;
import com.here.bean.Accusation;
import com.here.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/9/27 22:26
 */

public class AccusationPresenter extends BasePresenter<AccusationContract> {

    public void submit(Accusation report){
        if (TextUtils.isEmpty(report.getContent())){
            mvpView.showTips("请填写举报内容");
        }else if (TextUtils.isEmpty(report.getNumber())){
            mvpView.showTips("请填写联系电话");
        }else if(TextUtils.isEmpty(report.getKind())){
            mvpView.showTips("请选择举报类型");
        }else {
            mvpView.showLoading();
            report.setUser(mvpView.getUser());
            report.setSubmiter(BmobUser.getCurrentUser(User.class));
            report.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if ( e== null && mvpView!=null){
                        mvpView.stopLoading();
                        mvpView.submitSuccess();
                    }else if (mvpView!= null){
                        mvpView.stopLoading();
                        mvpView.showTips(e.getErrorCode() == 9016
                                ? "网络不给力" : e.getMessage());
                    }
                }
            });
        }
    }

}
