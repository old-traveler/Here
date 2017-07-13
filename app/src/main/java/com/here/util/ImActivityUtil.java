package com.here.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.common.StringUtils;
import com.here.HereApplication;
import com.here.R;
import com.here.bean.ApplyMessage;
import com.here.bean.ImActivity;
import com.here.bean.User;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hyc on 2017/7/4 16:19
 */

public class ImActivityUtil {

    public interface OnPublishListener{

        void success(String objectId);

        void fail(String error);

    }

    public interface OnGetNearByListener{
        void success(List<ImActivity> activities);
        void fail(String error);
    }

    public interface OnGetOneImActivityListener{
        void success(ImActivity imActivity);
        void fail(String error);
    }

    public static void publish(ImActivity imActivity, final OnPublishListener listener){
        imActivity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    listener.success(s);
                }else {
                    if (e.getErrorCode()==9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }


    /**
     * 缓存发布的新活动
     * @param imActivity
     */
    public static void cacheNewImActivity(ImActivity imActivity){
        Log.i("缓存",imActivity.getOverTime()+"  "+imActivity.getObjectId());
        SharedPreferences imActivitys = HereApplication.getContext()
                .getSharedPreferences("imActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = imActivitys.edit();
        editor.clear();
        editor.putString("publisherid",imActivity.getPublisher().getObjectId());
        editor.putString("objectid",imActivity.getObjectId());
        editor.putString("title",imActivity.getTitle());
        editor.putString("describe",imActivity.getDescribe());
        if (imActivity.getImages() ==null || imActivity.getImages().length==0){
            editor.putInt("image_count",0);
            editor.putString("images","");
        }else if(imActivity.getImages().length==1){
            editor.putInt("image_count",imActivity.getImages().length);
            editor.putString("images",imActivity.getImages()[0]);
        }else {
            editor.putInt("image_count",imActivity.getImages().length);
            String images = Arrays.toString(imActivity.getImages());
            images.substring(1,images.length()-1);
            editor.putString("images",images);
        }
        editor.putString("location",imActivity.getLocation());
        editor.putFloat("longitude", (float) imActivity.getLongitude());
        editor.putFloat("latitude", (float) imActivity.getLatitude());
        editor.putString("overTime",imActivity.getOverTime());
        editor.putInt("number",imActivity.getNumber());
        editor.putString("kind",imActivity.getKind());
        editor.putBoolean("isNeedApply",imActivity.isNeedApply());
        editor.putString("publisherTime",imActivity.getPublishTime());
        editor.commit();
    }

    public static ImActivity getImActivityInfo(User user){
        ImActivity imActivity = null;
        SharedPreferences preferences=HereApplication.getContext().getSharedPreferences("imActivity", Context.MODE_PRIVATE);
        Log.i("缓存",preferences.getString("publisherid","")+"  "+user.getObjectId()+"  "+JoinUtil.getTime()+"  "+preferences.getString("overTime","")+"  "+JoinUtil.getApplyUserId()+"  "+BmobUser.getCurrentUser(User.class).getObjectId());
        if (!TextUtils.isEmpty(preferences.getString("objectid",""))){
            if (preferences.getString("publisherid","").equals(user.getObjectId())
                    ||! TextUtils.isEmpty(JoinUtil.getTime())&& preferences.getString("overTime","").equals(JoinUtil.getTime())
                    && ! TextUtils.isEmpty(JoinUtil.getApplyUserId())
                    &&JoinUtil.getApplyUserId().equals(BmobUser.getCurrentUser(User.class).getObjectId())){
                imActivity = new ImActivity();
                imActivity.setPublisher(user);
                imActivity.setObjectId(preferences.getString("objectid",""));
                imActivity.setTitle(preferences.getString("title",""));
                imActivity.setDescribe(preferences.getString("describe",""));
                if (preferences.getInt("image_count",0)==0){
                    imActivity.setImages(null);
                }else if(preferences.getInt("image_count",0)==1){
                    String[] img=new String[1];
                    img[0]=preferences.getString("images","");
                    imActivity.setImages(img);
                }else {
                    imActivity.setImages(preferences.getString("images","").split(","));
                }
                imActivity.setLocation(preferences.getString("location",""));
                imActivity.setLongitude(preferences.getFloat("longitude",0.0f));
                imActivity.setLatitude(preferences.getFloat("latitude",0.0f));
                imActivity.setOverTime(preferences.getString("overTime",""));
                imActivity.setNumber(preferences.getInt("number",0));
                imActivity.setKind(preferences.getString("kind",""));
                imActivity.setNeedApply(preferences.getBoolean("isNeedApply",false));
                imActivity.setPublishTime(preferences.getString("publisherTime",""));
            }
        }

        return imActivity;
    }

    public static void clearImActivity(){
        SharedPreferences imActivitys = HereApplication.getContext()
                .getSharedPreferences("imActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = imActivitys.edit();
        editor.clear();
        editor.commit();
    }

    public static String getPublisherId(){
        SharedPreferences preferences=HereApplication.getContext()
                .getSharedPreferences("imActivity", Context.MODE_PRIVATE);
        return preferences.getString("publisherid","");
    }

    /**
     * 查询周围十公里的即时活动
     * @param latitude
     * @param longitude
     * @param listener
     */
    public static void getNearByImActivity(double latitude,double longitude, final OnGetNearByListener listener){
        double minLatitude = latitude - 0.09f;
        double maxLatitude = latitude + 0.09f;
        double minLongitude = longitude - 0.1016f;
        double maxLongitude = longitude + 0.1016f;

        BmobQuery<ImActivity> query = new BmobQuery();
        List<BmobQuery<ImActivity>> attrQuery = new ArrayList<BmobQuery<ImActivity>>();
        BmobQuery<ImActivity> query_1 = new BmobQuery();
        Calendar now = Calendar.getInstance();
        int hour  = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        query_1.addWhereGreaterThanOrEqualTo("CurrentTime",hour*60+minute);
        BmobQuery<ImActivity> query_2 = new BmobQuery();
        query_2.addWhereGreaterThanOrEqualTo("longitude",minLongitude);
        BmobQuery<ImActivity> query_3 = new BmobQuery();
        query_3.addWhereLessThanOrEqualTo("longitude",maxLongitude);
        BmobQuery<ImActivity> query_4 = new BmobQuery();
        query_4.addWhereGreaterThanOrEqualTo("latitude",minLatitude);
        BmobQuery<ImActivity> query_5 = new BmobQuery();
        query_5.addWhereLessThanOrEqualTo("latitude",maxLatitude);
        BmobQuery<ImActivity> query_6 = new BmobQuery();
        query_6.addWhereEqualTo("publishDate",now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH));
        attrQuery.add(query_1);
        attrQuery.add(query_2);
        attrQuery.add(query_3);
        attrQuery.add(query_4);
        attrQuery.add(query_5);
        attrQuery.add(query_6);
        query.and(attrQuery);
        query.setLimit(100);
        query.include("publisher");
        query.findObjects(new FindListener<ImActivity>() {
            @Override
            public void done(List<ImActivity> list, BmobException e) {
                if (e == null){
                    listener.success(list);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getErrorCode()+e.getMessage());
                    }
                }
            }
        });

    }

    public static void sendImActivityApply(User user,User user1,final UserUtil.OnDealListener listener){
        BmobIMUserInfo info = new BmobIMUserInfo();
        if (TextUtils.isEmpty(user.getNickname())){
            info.setName(user.getUsername());
        }else {
            info.setName(user.getNickname());
        }
        info.setUserId(user.getObjectId());
        if (!TextUtils.isEmpty(user.getHeadImageUrl())){
            info.setAvatar(user.getHeadImageUrl());
        }else {
            info.setAvatar("dsaodaso");
        }

        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        ApplyMessage msg = new ApplyMessage();
        msg.setContent(user1.getObjectId());
        msg.setMsgType("apply");
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null){
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }


    public static void respondApply(BmobIMUserInfo info,String objectId, final UserUtil.OnDealListener listener){
        Log.i("测试",info.getUserId()+"  "+info.getName()+"  "+info.getAvatar());
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        ApplyMessage msg = new ApplyMessage();
        msg.setContent(objectId);
        msg.setMsgType("response");
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null){
                    listener.success();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });

    }

    /**
     * 通过id查询单个即时活动信息
     * @param objectId
     * @param listener
     */
    public static void queryOneImActivity(String objectId, final OnGetOneImActivityListener listener){
        BmobQuery<ImActivity> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<ImActivity>() {
            @Override
            public void done(ImActivity imActivity, BmobException e) {
                if (e==null){
                    listener.success(imActivity);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    public static void deleteImActivity(ImActivity imActivity, final UserUtil.OnDealListener listener){
        imActivity.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    listener.success();
                    clearImActivity();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(HereApplication.getContext().getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }




}
