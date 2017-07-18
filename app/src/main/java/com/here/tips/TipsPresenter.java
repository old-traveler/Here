package com.here.tips;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.Tip;
import com.here.bean.User;
import com.here.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/5 12:42
 */

public class TipsPresenter extends BasePresenter<TipsContract> {


    public void loadTips(){
        User user = BmobUser.getCurrentUser(User.class);
        List<Tip> tips = new ArrayList<>();
        String[] tips_name=HereApplication.getContext().getResources().getStringArray(R.array.tip_name);
        String[] tips_slogan=HereApplication.getContext().getResources().getStringArray(R.array.tip_slogan);
        int[]  bg=HereApplication.getContext().getResources().getIntArray(R.array.tips_bg);
        for (int i = 0; i < 24; i++) {
            Tip tip=new Tip();
            tip.setHave(false);
            if (user.getTips() != null) {
                for (String s : user.getTips()) {
                    if (tips_name[i].equals(s)){
                        tip.setHave(true);
                        break;
                    }
                }
            }
            tip.setColor(bg[i]);
            tip.setName(tips_name[i]);
            tip.setSlogan(tips_slogan[i]);
            tips.add(tip);
        }
        mvpView.initData(tips);
    }

    public void uploadTips(){
        boolean isNeedUpdate = false;
        String[] tips = mvpView.getTips();
        User user = BmobUser.getCurrentUser(User.class);
        if (user.getTips() != null && user.getTips().length == tips.length){
            for (int i = 0; i < tips.length; i++) {
                if (!tips[i].equals(user.getTips()[i])){
                    isNeedUpdate = true;
                    break;
                }
            }
        }else {
            isNeedUpdate = true;
        }
        if (isNeedUpdate){
            mvpView.showLoading();
            user.setTips(mvpView.getTips());
            UserUtil.updateUserInfo(user, new UserUtil.OnDealListener() {
                @Override
                public void success() {
                    if (mvpView != null) {
                        mvpView.stopLoading();
                        mvpView.loadSuccess();
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null) {
                        mvpView.stopLoading();
                        mvpView.loadFail(error);
                    }
                }
            });
        }

    }


}
