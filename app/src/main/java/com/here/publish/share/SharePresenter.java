package com.here.publish.share;

import android.text.TextUtils;

import com.here.base.BasePresenter;
import com.here.bean.Mood;
import com.here.bean.User;
import com.here.util.FileUtil;
import com.here.util.TinyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hyc on 2017/7/14 12:08
 */

public class SharePresenter extends BasePresenter<ShareContract> {

    public void publish(){
        Mood mood = mvpView.getMoodInfo();
        if (TextUtils.isEmpty(mood.getTitle())){
            mvpView.fail("请填写标题");
            return;
        }else if (TextUtils.isEmpty(mood.getContent())){
            mvpView.fail("请填写好内容");
            return;
        }else if (TextUtils.isEmpty(mood.getKind())){
            mvpView.fail("请选择类型");
            return;
        }
        mvpView.showLoading();
        List<String> img = mvpView.getImages();
        if (img.size()>0){
            String[] images = new String[img.size()];
            for (int i = 0; i < img.size(); i++) {
                images[i] = img.get(i);
            }
            TinyUtil.batchCompress(images, new TinyUtil.OnBatchCompressListener() {
                @Override
                public void success(String[] out) {
                    uploadImages(out);
                }

                @Override
                public void fail() {
                    mvpView.stopLoading();
                    mvpView.publishFail("压缩图片失败");
                }
            });
        }else {
            uploadMood(mvpView.getMoodInfo());
        }
    }

    public void uploadImages(String[] out){
        FileUtil.uploadBatch(out, new FileUtil.OnUploadBatchListener() {
            @Override
            public void success(List<String> images) {
                String[] image = new String[images.size()];
                for (int i = 0; i < images.size(); i++) {
                    image[i] = images.get(i);
                }
                mvpView.getMoodInfo().setImages(image);
                uploadMood(mvpView.getMoodInfo());
            }

            @Override
            public void fail(String error) {
                mvpView.stopLoading();
                mvpView.publishFail(error);
            }
        });
    }


    public void uploadMood(Mood mood){
        mood.setPublishTime(System.currentTimeMillis());
        mood.setPublisher(BmobUser.getCurrentUser(User.class));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD-hh:mm");
        mood.setPublisherDate(format.format(new Date(System.currentTimeMillis())));
        mood.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    mvpView.stopLoading();
                    mvpView.publishSuccess();
                }else {
                    mvpView.stopLoading();
                    if (e.getErrorCode() == 9016){
                        mvpView.publishFail("网络不给力");
                    }else {
                        mvpView.publishFail(e.getErrorCode()+e.getMessage());
                    }
                }
            }
        });
    }

}
