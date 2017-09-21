package com.here.blacklist;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.Blacklist;
import com.here.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hyc on 2017/9/21 22:06
 */

public class BlacklistPresenter extends BasePresenter<BlacklistContract> {

    public void loadBlacklist(){
        BmobQuery<Blacklist> query = new BmobQuery<>();
        mvpView.showLoading();
        query.include("blacklistUser");
        query.findObjects(new FindListener<Blacklist>() {
            @Override
            public void done(List<Blacklist> list, BmobException e) {
                if (mvpView != null && e == null){
                    mvpView.stopLoading();
                    List<User> users = new ArrayList<>();
                    for (Blacklist blacklist : list) {
                        users.add(blacklist.getBlacklistUser());
                    }
                    mvpView.setBlacklist(users);
                }else if (mvpView != null){
                    mvpView.stopLoading();
                    if (e.getErrorCode() == 9016){
                        mvpView.loadFail(HereApplication.getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        mvpView.loadFail(e.getMessage());
                    }
                }
            }
        });
    }
}
