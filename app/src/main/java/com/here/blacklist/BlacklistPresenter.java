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
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hyc on 2017/9/21 22:06
 */

public class BlacklistPresenter extends BasePresenter<BlacklistContract> {

    private List<String> id;

    public void loadBlacklist(){
        BmobQuery<Blacklist> query = new BmobQuery<>();
        mvpView.showLoading();
        query.include("blacklistUser");
        query.findObjects(new FindListener<Blacklist>() {
            @Override
            public void done(List<Blacklist> list, BmobException e) {
                if (mvpView != null && e == null){
                    mvpView.stopLoading();
                    id = new ArrayList<>();
                    List<User> users = new ArrayList<>();
                    for (Blacklist blacklist : list) {
                        users.add(blacklist.getBlacklistUser());
                        id.add(blacklist.getObjectId());
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

    public void removeBlacklist(final int position){
        Blacklist blacklist = new Blacklist();
        blacklist.setObjectId(id.get(position));
        mvpView.showLoading();
        blacklist.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (mvpView == null) return;
                mvpView.stopLoading();
                if (e == null){
                    mvpView.removeBlacklist(position);
                }else if (e.getErrorCode() == 9016){
                    mvpView.showTips("网络不给力");
                }else {
                    mvpView.showTips(e.getMessage());
                }
            }
        });
    }
}
