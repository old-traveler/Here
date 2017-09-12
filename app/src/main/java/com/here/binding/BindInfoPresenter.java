package com.here.binding;

import com.here.R;
import com.here.base.BasePresenter;
import com.here.util.UserUtil;

/**
 * Created by hyc on 2017/9/8 14:44
 */

public class BindInfoPresenter extends BasePresenter<BindInfoContract> {

    /**
     * 检查用户的填写的信息是否完整、是否同意用户协议。
     * 如果check正常将进行下一步信息绑定，否则提示用户
     */
    public void checkUserInfo(){
        if (mvpView.isFilled()){
            if (mvpView.idIsRight()){
                if (mvpView.isAgreeDeal()){
                    bindUserInfo();
                }else {
                    mvpView.showTips(R.string.tips_agree_deal);
                }
            }else {
                mvpView.showTips(R.string.tips_id_error);
            }
        }else {
            mvpView.showTips(R.string.tips_info_empty);
        }
    }

    /**
     * 绑定用户部分关键信息,上传到云端并更新当前用户的本地信息
     */
    public void bindUserInfo(){
        mvpView.showLoading();
        UserUtil.updateUserInfo(mvpView.getUser(), new UserUtil.OnDealListener() {
            @Override
            public void success() {
                mvpView.stopLoading();
                mvpView.bindSuccess();
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.bindFail(error);
            }
        });
    }


}
