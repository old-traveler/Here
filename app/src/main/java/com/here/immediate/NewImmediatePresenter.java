package com.here.immediate;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.here.adapter.PublishImageAdapter;
import com.here.base.BasePresenter;
import com.here.bean.User;
import com.here.util.FileUtil;
import com.here.util.ImActivityUtil;
import com.here.util.TinyUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by hyc on 2017/7/3 22:55
 */

public class NewImmediatePresenter extends BasePresenter<NewImmediateContract> implements AMapLocationListener{

    public void showImages(List<String> images){
        mvpView.initRecycler(images);
    }

    public void setAddImageListener(){
        mvpView.setListener(new PublishImageAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                mvpView.choicePic();
            }
        });
    }

    public void getLocation(){
        mvpView.showLoading();
        mvpView.getLocation();
    }

    public void publishNewImActivity(){
        if (!mvpView.getImActivity().isEmpty()){
            if (mvpView.getActivityNumber()==0 || mvpView.getActivityNumber()>20){
                mvpView.noFilled("参与人数应在1~20之间");
            }else {
                mvpView.showLoading();
                if (mvpView.getImActivity().getImages() != null && mvpView.getImActivity().getImages().length>0){
                    TinyUtil.batchCompress(mvpView.getImActivity().getImages(), new TinyUtil.OnBatchCompressListener() {
                        @Override
                        public void success(String[] out) {
                            if (out.length==mvpView.getImActivity().getImages().length){
                                FileUtil.uploadBatch(out, new FileUtil.OnUploadBatchListener() {
                                    @Override
                                    public void success(List<String> images) {
                                        String[] image=new String[images.size()];
                                        for (int i = 0; i < images.size(); i++) {
                                            image[i]=images.get(i);
                                        }
                                        mvpView.getImActivity().setImages(image);
                                        upload();
                                    }

                                    @Override
                                    public void fail(String error) {
                                        mvpView.stopLoading();
                                        mvpView.noFilled("上传失败"+error);
                                    }
                                });
                            }
                        }

                        @Override
                        public void fail() {
                            mvpView.stopLoading();
                            mvpView.noFilled("上传失败");
                        }
                    });
                }else {
                    upload();
                }

            }
        }else if (TextUtils.isEmpty(mvpView.getImActivity().getLocation())){
            mvpView.noFilled("无法获取当前位置信息，请先定位");
        }else {
            mvpView.noFilled("请先将信息填写完毕");
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode()==0){
            mvpView.setLocationMessage(aMapLocation.getDistrict()+" "+aMapLocation.getStreet()+" "+aMapLocation.getPoiName()+"附近");
            mvpView.stopLoading();
            mvpView.getImActivity().setLatitude(aMapLocation.getLatitude());
            mvpView.getImActivity().setLongitude(aMapLocation.getLongitude());
        }else {
            mvpView.getLocationFail();
            mvpView.stopLoading();
        }
    }

    public void upload(){
        mvpView.getImActivity().setPublisher(BmobUser.getCurrentUser(User.class));
        mvpView.getImActivity().setNumber(mvpView.getActivityNumber());
        mvpView.getImActivity().setCurrentTime(Integer.parseInt(mvpView.getImActivity().getOverTime().split("-")[3]
                .split(":")[0])*60+Integer.parseInt(mvpView.getImActivity().getOverTime().split("-")[3].split(":")[1]));
        mvpView.getImActivity().setPublishDate(mvpView.getImActivity().getOverTime().split("-")[0]+"-"+mvpView.getImActivity().getOverTime().split("-")[1]+"-"+mvpView.getImActivity().getOverTime().split("-")[2]);
        ImActivityUtil.publish(mvpView.getImActivity(), new ImActivityUtil.OnPublishListener() {
            @Override
            public void success(String objectId) {
                mvpView.getImActivity().setObjectId(objectId);
                ImActivityUtil.cacheNewImActivity(mvpView.getImActivity());
                mvpView.stopLoading();
                mvpView.uploadSuccess();
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.noFilled(error);
            }
        });
    }
}
