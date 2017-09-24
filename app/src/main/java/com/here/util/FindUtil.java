package com.here.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
import com.here.R;
import com.here.bean.FindImage;
import com.here.bean.User;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hyc on 2017/9/23 16:43
 */

public class FindUtil {


    public interface OnFindListener{
        void hadJoin();
        void noJoin();
    }

    public interface OnQueryListener{
        void onSuccess(List<FindImage> findImages);
        void onError(String error);
    }

    /**
     * 判断当前用户是否加入发现活动
     * 首先从本地缓存判断，如果没有
     * 则查询云端记录，如有同步本地
     * 如果也没有则提示用户是否加入
     * @param listener 查询监听
     */
    public static void isJoinFind(final OnFindListener listener){
        if (TextUtils.isEmpty(getRecordUrlCache())){
            BmobQuery<FindImage> query = new BmobQuery<>();
            query.addWhereEqualTo("master", BmobUser.getCurrentUser(User.class));
            query.setLimit(1);
            query.findObjects(new FindListener<FindImage>() {
                @Override
                public void done(List<FindImage> list, BmobException e) {
                    if (e== null){
                        if (list.size()>0){
                            listener.hadJoin();
                            addRecordCache(list.get(0).getObjectId(),list.get(0).getUrl());
                        }else {
                            listener.noJoin();
                        }
                    }else {
                        if (e.getErrorCode() == 101){
                            listener.noJoin();
                        }
                        Log.i("Error",e.getErrorCode()+" "+e.getMessage());
                    }
                }
            });
        }else if (!getRecordUrlCache().equals("refuse")){
            listener.hadJoin();
        }

    }

    /**
     * 加入发现专栏
     * @param url 图片地址
     * @param listener 处理监听
     */
    public static void joinFind(final String url,int[] size, final UserUtil
            .OnDealListener listener){
        FindImage findImage = new FindImage();
        findImage.setMaster(BmobUser.getCurrentUser(User.class));
        findImage.setUrl(url);
        findImage.setWidth(size[0]);
        findImage.setHeight(size[1]);
        findImage.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.success();
                    addRecordCache(s,url);
                }else {
                    listener.fail(e.getMessage());
                }
            }
        });
    }


    /**
     * 添加加入发现记录缓存
     * 如果填入refuse则为拒绝
     * 加入，进入时不再提醒
     * @param url 图片地址
     */
    public static void addRecordCache(String objectId,String url){
        SharedPreferences find = HereApplication.getContext()
                .getSharedPreferences("find", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = find.edit();
        editor.putString("url",url);
        editor.putString("objectId",objectId);
        editor.commit();
    }


    /**
     * 获得加入发现图片地址缓存
     * @return 加入发现图片地址缓存信息
     */
    public static String getRecordUrlCache(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("find", Context.MODE_PRIVATE);
        return preferences.getString("url","");
    }

    /**
     * 获得加入信息Id信息缓存
     * @return 加入信息对象的objectId
     */
    public static String getRecordIdCache(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("find", Context.MODE_PRIVATE);
        return preferences.getString("objectId","");
    }

    public static void updateUrlRecordCache(String url){
        SharedPreferences find = HereApplication.getContext()
                .getSharedPreferences("find", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = find.edit();
        editor.putString("url",url);
        editor.commit();
    }

    /**
     * 更新当前用户发现的信息
     * @param url  图片地址
     * @param listener 更新监听
     */
    public static void updateFind(final String url , int[] size,final UserUtil
            .OnDealListener listener){
        FindImage image = new FindImage();
        image.setUrl(url);
        image.setWidth(size[0]);
        image.setHeight(size[1]);
        image.update(getRecordIdCache(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    updateUrlRecordCache(url);
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail("没有网络");
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    public static void queryFinds(int page, final OnQueryListener listener){
        BmobQuery<FindImage> query = new BmobQuery<>();
        query.setLimit(20);
        query.setSkip(page * 20);
        query.order("-updatedAt");
        query.include("master");
        query.findObjects(new FindListener<FindImage>() {
            @Override
            public void done(List<FindImage> list, BmobException e) {
                if (e == null){
                    listener.onSuccess(list);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.onError("网络不给力");
                    }else {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 当用户退出登录时清除缓存
     */
    public static void cleanRecord(){
        SharedPreferences find = HereApplication.getContext()
                .getSharedPreferences("find", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = find.edit();
        editor.clear();
        editor.commit();
    }


}
