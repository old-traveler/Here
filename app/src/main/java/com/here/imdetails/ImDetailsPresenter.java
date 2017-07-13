package com.here.imdetails;

import com.here.HereApplication;
import com.here.R;
import com.here.base.BasePresenter;
import com.here.bean.Join;
import com.here.bean.User;
import com.here.nearby.NearbyPresenter;
import com.here.util.ImActivityUtil;
import com.here.util.ImUtil;
import com.here.util.JoinUtil;
import com.here.util.NetworkState;
import com.here.util.UserUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/8 09:47
 */

public class ImDetailsPresenter extends BasePresenter<ImDetailsContract> {


    public void loadingData(){
        mvpView.loadingData(mvpView.getImActivity());
    }

    public void apply(){
        if (NearbyPresenter.going){
            mvpView.fail("您正处于活动进行中");
            return;

        }
        if (JoinUtil.isTimeAfterCurrent()){
            if (JoinUtil.isApply()){
                mvpView.fail("您正处于申请活动中，请稍后再试");
            }else {
                mvpView.fail("您正处于活动进行中");
            }
        }else {

            if (mvpView.getImActivity().isNeedApply()){
                if (ImUtil.isConnected){
                    mvpView.showLoading();
                    ImActivityUtil.sendImActivityApply(mvpView.getImActivity().getPublisher(),BmobUser.getCurrentUser(User.class), new UserUtil.OnDealListener() {
                        @Override
                        public void success() {
                            mvpView.stopLoading();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                            JoinUtil.joinNewImActivity(true,simpleDateFormat.format(new Date((System.currentTimeMillis()+1000*60*10L))));
                            mvpView.sendApplySuccess();
                        }

                        @Override
                        public void fail(String error) {
                            mvpView.stopLoading();
                            mvpView.fail(error);
                        }
                    });
                }else {
                    if (NetworkState.netIsActivity){
                        mvpView.fail("服务器连接失败，请稍后再试");
                        ImUtil.connectServer();
                    }else {
                        mvpView.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }
                }
            }else {
                mvpView.showLoading();
                Join join =new Join();
                join.setJoinUser(BmobUser.getCurrentUser(User.class));
                join.setImActivity(mvpView.getImActivity());
                JoinUtil.uploadNewJoin(join, new UserUtil.OnDealListener() {
                    @Override
                    public void success() {
                        JoinUtil.cacheApplyUserId(BmobUser.getCurrentUser(User.class).getObjectId());
                        ImActivityUtil.cacheNewImActivity(mvpView.getImActivity());
                        JoinUtil.joinNewImActivity(false, mvpView.getImActivity().getOverTime());
                        mvpView.stopLoading();
                        mvpView.joinSuccess();
                    }

                    @Override
                    public void fail(String error) {
                        mvpView.stopLoading();
                        mvpView.joinFail(error);
                    }
                });
            }


        }
    }


}
