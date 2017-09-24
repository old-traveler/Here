package com.here.follow.join;

import android.text.TextUtils;
import android.util.Log;

import com.here.base.BasePresenter;
import com.here.util.FileUtil;
import com.here.util.FindUtil;
import com.here.util.TinyUtil;
import com.here.util.UserUtil;

import java.util.List;

/**
 * Created by hyc on 2017/9/23 17:37
 */

public class JoinFindPresenter extends BasePresenter<JoinFindContract> {


    /**
     * 上传信息，如果没有添加过云端记录
     * 则上传云端记录，上传过则更新记录
     * @param path 图片路径
     */
    public void upload(String path){
        String[] source = new String[1];
        source[0] = path;
        mvpView.showLoading();
        TinyUtil.batchCompress(source, new TinyUtil
                .OnBatchCompressListener() {
            @Override
            public void success(String[] out) {
                uploadImage(out);
            }

            @Override
            public void fail() {
                mvpView.stopLoading();
                mvpView.showTip("图片压缩失败");
            }
        });
    }

    /**
     * 上传图片
     * @param out 图片
     */
    public void uploadImage(String[] out){
        final int[] size = FileUtil.getFileSize(out[0]);
        FileUtil.uploadBatch(out, new FileUtil
                .OnUploadBatchListener() {
            @Override
            public void success(List<String> images) {
                updateFind(size,images.get(0));
            }

            @Override
            public void fail(String error) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.showTip(error);
                }
            }
        });
    }

    /**
     * 查询用户是否加入发现
     */
    public void queryIsJoin(){
        mvpView.showLoading();
        FindUtil.isJoinFind(new FindUtil.OnFindListener() {
            @Override
            public void hadJoin() {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.hadJoin();
                }

            }

            @Override
            public void noJoin() {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.hadNoJoin();
                    Log.i("TAG","查询不到Join");
                }
            }
        });
    }

    /**
     * 更新记录
     * @param url 图片云端地址
     */
    public void updateFind(int[] size , final String url){
        if (TextUtils.isEmpty(FindUtil.getRecordIdCache())){
            FindUtil.joinFind(url,size, new UserUtil
                    .OnDealListener() {
                @Override
                public void success() {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.uploadImageSuccess(url);
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.showTip(error);
                    }
                }
            });
        }else {
            FindUtil.updateFind(url,size ,new UserUtil
                    .OnDealListener() {
                @Override
                public void success() {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.uploadImageSuccess(url);
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.showTip(error);
                    }
                }
            });
        }
    }


}
