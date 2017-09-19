package com.here.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
import com.here.bean.ImActivity;
import com.here.bean.ImageAddress;
import com.here.bean.Join;
import com.here.bean.User;
import com.here.db.MyDatabaseHelper;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobUser;

/**
 * 数据库工具类 Created by hyc on 2017/9/16 15:20
 */

public class DbUtil {

    /**
     * 数据库辅助对象
     */
    private MyDatabaseHelper myDatabaseHelper;
    /**
     * 数据库工具实例
     */
    private static DbUtil dbUtil;
    /**
     * 数据库操作对象
     */
    private SQLiteDatabase db;

    public static synchronized DbUtil getInstance(){
        return dbUtil == null ? new DbUtil() : dbUtil;
    }

    public DbUtil(){
        myDatabaseHelper = new MyDatabaseHelper(HereApplication
                .getContext(),"ComHere.db",null,1);
        db = myDatabaseHelper.getWritableDatabase();
    }

    /**
     * 刷新当前用户的关注与粉丝信息
     * 如果有新增关注或粉丝信息，则
     * 对应在本地数据库中添加新增信息
     * 如果有取消关注的信息减少，则删
     * 除本地数据库中的对应记录
     * @param isFollow true 为关注信息，false为粉丝信息
     * @param users 云端的关注或是是粉丝用户信息
     */
    public void refreshRecode(boolean isFollow , List<User> users){
        List<User> old = queryCurrentUserFollowOrFans(isFollow);
        for (User user : old) {
            boolean isExists = false;
            for (User user1 : users) {
                if (user.getObjectId().equals(
                        user1.getObjectId())){
                    isExists = true;
                    break;
                }
            }
            if (!isExists){
                deleteFollow(user.getObjectId(),isFollow);
            }
        }
        for (User user : users) {
            addFollow(user,isFollow);
        }
    }

    /**
     * 添加一条关注或者粉丝记录
     * @param user   用户信息
     * @param isFollow 是关注还是粉丝
     */
    private void addFollow(User user , boolean isFollow ){
        ContentValues values = new ContentValues();
        values.put("user_id" , user.getObjectId());
        values.put("nickname",user.getNickname());
        values.put("sex",user.getSex());
        values.put("age",user.getAge());
        values.put("birth",user.getDateOfBirth());
        values.put("introduction",user.getIntroduction());
        values.put("head",user.getHeadImageUrl());
        values.put("address",user.getAddress());
        values.put("show_number",user.isShowNumber());
        values.put("show_age",user.isShowAge());
        values.put("show_birth",user.isShowDataOfBirth());
        values.put("background",user.getBackgroundUrl());
        values.put("relevant_id", BmobUser
                .getCurrentUser().getObjectId());
        values.put("is_follow",isFollow ? 1:0);
        values.put("tips",stringArrayToString(user.getTips()));

    }

    /**
     * 添加一个用户信息
     * @param user 用户信息对象
     */
    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put("user_id" , user.getObjectId());
        values.put("nickname",user.getNickname());
        values.put("sex",user.getSex());
        values.put("age",user.getAge());
        values.put("birth",user.getDateOfBirth());
        values.put("introduction",user.getIntroduction());
        values.put("head",user.getHeadImageUrl());
        values.put("address",user.getAddress());
        values.put("show_number",user.isShowNumber());
        values.put("show_age",user.isShowAge());
        values.put("show_birth",user.isShowDataOfBirth());
        values.put("background",user.getBackgroundUrl());
        values.put("relevant_id", "publisher");
        values.put("is_follow",3);
        values.put("tips",stringArrayToString(user.getTips()));
        db.insert("User",null,values);
    }

    /**
     * 将字符数组转化为String类型存储
     * @param tips 字符数组
     * @return  字符串数据
     */
    public String stringArrayToString(String[] tips){
        String tip = "";
        if (tips != null && tips.length > 0){
            if (tips.length == 2){
                return tips[0] + "&" + tips[1];
            }
            for (int i = 0; i < tips.length; i++) {
                if (i == 0){
                    tip = tip + tips[i];
                }else {
                    tip = tip + "&" + tips[i];
                }
            }
        }
        return tip;
    }


    /**
     * 查询当前用户的关注用户或者粉丝的用户信息
     * @param isFollow true为查询关注用户，false为查询粉丝信息
     * @return 查询到的用户信息
     */
    public List<User> queryCurrentUserFollowOrFans(boolean isFollow){
        List<User> users = null;
        try {
            users = new ArrayList<>();
            Cursor cursor = db.query("User",null, null
                    ,null,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    if (!cursor.getString(cursor.getColumnIndex(
                            "relevant_id")).equals(BmobUser
                            .getCurrentUser().getObjectId()) ){
                        continue;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(
                            "is_follow")) == 0 && isFollow || (cursor
                            .getInt(cursor.getColumnIndex("is_follow"))
                            == 1 && !isFollow)){
                        continue;
                    }
                    User user = new User();
                    user.setObjectId(cursor.getString(cursor
                            .getColumnIndex("user_id")));
                    user.setNickname(cursor.getString(cursor
                            .getColumnIndex("nickname")));
                    user.setSex(cursor.getString(cursor
                            .getColumnIndex("sex")));
                    user.setAge(cursor.getInt(cursor
                            .getColumnIndex("age")));
                    user.setDateOfBirth(cursor.getString
                            (cursor.getColumnIndex("birth")));
                    user.setIntroduction(cursor.getString(cursor
                            .getColumnIndex("introduction")));
                    user.setHeadImageUrl(cursor.getString
                            (cursor.getColumnIndex("head")));
                    user.setAddress(cursor.getString(cursor
                            .getColumnIndex("address")));
                    user.setShowNumber(cursor.getString(cursor
                            .getColumnIndex("show_number")).equals("1"));
                    user.setShowDataOfBirth(cursor.getString
                            (cursor.getColumnIndex("show_birth")).equals("1"));
                    user.setShowAge(cursor.getString(cursor
                            .getColumnIndex("show_age")).equals("1"));
                    user.setBackgroundUrl(cursor.getString(
                            cursor.getColumnIndex("background")));
                    user.setTips(stringToStringArray(cursor.getString(
                            cursor.getColumnIndex("tips"))));
                    users.add(user);
                }while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return users;
    }

    /**
     * 通过用户id查询用户信息
     * @param id 需查询的用户id
     * @return 查询到的用户信息
     */
    public User queryUser(String id){
        User user= new User();
        Cursor cursor = db.query("User",null, null
                ,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                if (id.equals(cursor.getString(cursor
                        .getColumnIndex("user_id")))) {
                    user.setObjectId(cursor.getString(
                            cursor.getColumnIndex("user_id")));
                    user.setNickname(cursor.getString(
                            cursor.getColumnIndex("nickname")));
                    user.setSex(cursor.getString(
                            cursor.getColumnIndex("sex")));
                    user.setAge(cursor.getInt(
                            cursor.getColumnIndex("age")));
                    user.setDateOfBirth(cursor.getString
                            (cursor.getColumnIndex("birth")));
                    user.setIntroduction(cursor.getString(cursor
                            .getColumnIndex("introduction")));
                    user.setHeadImageUrl(cursor.getString
                            (cursor.getColumnIndex("head")));
                    user.setAddress(cursor.getString(cursor
                            .getColumnIndex("address")));
                    user.setShowNumber(cursor.getString(cursor
                            .getColumnIndex("show_number")).equals("1"));
                    user.setShowDataOfBirth(cursor.getString
                            (cursor.getColumnIndex("show_birth")).equals("1"));
                    user.setShowAge(cursor.getString(cursor
                            .getColumnIndex("show_age")).equals("1"));
                    user.setBackgroundUrl(cursor.getString(
                            cursor.getColumnIndex("background")));
                    user.setTips(stringToStringArray(cursor.getString(
                            cursor.getColumnIndex("tips"))));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return user;
    }

    /**
     * 将String类型分割转化为数组
     * @param tips 字符串数据
     * @return  字符串数组数据
     */
    public String[] stringToStringArray(String tips){
        if (TextUtils.isEmpty(tips)){
            return null;
        }else {
            return tips.split("&");
        }
    }

    /**
     * 添加一条参加活动记录
     * @param join 参加对象信息
     */
    public void addJoin(Join join){
        addAppointment(join.getImActivity());
        ContentValues values = new ContentValues();
        values.put("join_id",BmobUser
                .getCurrentUser().getObjectId());
        values.put("activity_id",join
                .getImActivity().getObjectId());
        db.insert("Joins",null,values);
    }

    /**
     * 更新本地数据库中对应用户的发布记录
     * 如果有新增记录将存入数据中，如果云端
     * 记录删除，对应的删除本地数据库中对应的信息
     * @param user   对应用户
     * @param imActivities 该用户对应云端发布记录信息
     */
    public void refreshUserPublish(User user, List<ImActivity> imActivities){
        List<ImActivity> old = queryMyPublisher(user);
        for (ImActivity imActivity : old) {
            boolean isExists = false;
            for (ImActivity activity : imActivities) {
                if (imActivity.getObjectId().equals(
                        activity.getObjectId())){
                    isExists = true;
                    break;
                }
            }
            if (!isExists){
                deleteImActivity(imActivity.getObjectId());
            }
        }

        for (ImActivity imActivity : imActivities) {
            addAppointment(imActivity);
        }
    }

    /**
     * 根据即时活动的id删除数据库中的记录
     * @param id 即时活动对应的id
     */
    private void deleteImActivity(String id) {
        db.delete("ImActivity","id = ? ", new String[]{id});
    }


    /**
     * 添加一条预约活动记录
     * @param activity 活动信息
     */
    public void addAppointment(ImActivity activity){
        ContentValues values = new ContentValues();
        values.put("id",activity.getObjectId());
        if (activity.getPublisher() != null){
            addUser(activity.getPublisher());
            values.put("uid",activity.getPublisher().getObjectId());
        }else {
            values.put("uid","");
        }
        values.put("publish_date",activity.getPublishDate());
        values.put("publish_time",activity.getPublishTime());
        values.put("is_apply",activity.isNeedApply());
        values.put("title",activity.getTitle());
        values.put("describe",activity.getDescribe());
        values.put("location",activity.getLocation());
        values.put("longitude",activity.getLongitude());
        values.put("latitude",activity.getLatitude());
        values.put("images",stringArrayToString(activity.getImages()));
        values.put("kind",activity.getKind());
        values.put("number",activity.getNumber());
        values.put("current_time",activity.getCurrentTime());
        values.put("over_time",activity.getOverTime());
        db.insert("ImActivity",null,values);
    }

    /**
     * 查询用户的发布活动
     * @param user 查询用户条件
     * @return 该用户所对用的发布活动信息
     */
    public List<ImActivity> queryMyPublisher(User user){
        List<ImActivity> imActivities = new ArrayList<>();
        Cursor cursor = db.query("ImActivity",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (user.getObjectId().equals(cursor.getString(
                        cursor.getColumnIndex("uid")))){
                    ImActivity imActivity = new ImActivity();
                    imActivity.setPublisher(user);
                    imActivity.setObjectId(cursor.getString(
                            cursor.getColumnIndex("id")));
                    imActivity.setPublishDate(cursor.getString(
                            cursor.getColumnIndex("publish_date")));
                    imActivity.setPublishTime(cursor.getString(
                            cursor.getColumnIndex("publish_time")));
                    imActivity.setNeedApply(cursor.getString(
                            cursor.getColumnIndex("is_apply")).equals("1"));
                    imActivity.setTitle(cursor.getString(
                            cursor.getColumnIndex("title")));
                    imActivity.setDescribe(cursor.getString(
                            cursor.getColumnIndex("describe")));
                    imActivity.setLocation(cursor.getString(
                            cursor.getColumnIndex("location")));
                    imActivity.setLongitude(cursor.getDouble(
                            cursor.getColumnIndex("longitude")));
                    imActivity.setLatitude(cursor.getDouble(
                            cursor.getColumnIndex("latitude")));
                    imActivity.setImages(stringToStringArray(cursor
                            .getString(cursor.getColumnIndex("images"))));
                    imActivity.setKind(cursor.getString(
                            cursor.getColumnIndex("kind")));
                    imActivity.setNumber(cursor.getInt(cursor
                            .getColumnIndex("number")));
                    imActivity.setCurrentTime(cursor.getInt
                            (cursor.getColumnIndex("current_time")));
                    imActivity.setOverTime(cursor.getString(
                            cursor.getColumnIndex("over_time")));
                    imActivities.add(imActivity);
                    Log.i("TAG","即时活动图片"+cursor.getString(cursor.getColumnIndex("images")));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return imActivities;
    }

    /**
     * 通过活动id查询即时活动信息
     * @param id 查询活动的id
     * @return 活动信息
     */
    public ImActivity queryImActivity(String id){
        ImActivity imActivity = new ImActivity();
        Cursor cursor = db.query("ImActivity",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id.equals(cursor.getString(cursor
                        .getColumnIndex("id")))){
                    imActivity.setPublisher(queryUser(cursor
                            .getString(cursor.getColumnIndex("uid"))));
                    imActivity.setObjectId(cursor.getString
                            (cursor.getColumnIndex("id")));
                    imActivity.setPublishDate(cursor.getString
                            (cursor.getColumnIndex("publish_date")));
                    imActivity.setPublishTime(cursor.getString
                            (cursor.getColumnIndex("publish_time")));
                    imActivity.setNeedApply(cursor.getString(cursor
                            .getColumnIndex("is_apply")).equals("1"));
                    imActivity.setTitle(cursor.getString(
                            cursor.getColumnIndex("title")));
                    imActivity.setDescribe(cursor.getString(
                            cursor.getColumnIndex("describe")));
                    imActivity.setLocation(cursor.getString(
                            cursor.getColumnIndex("location")));
                    imActivity.setLongitude(cursor.getDouble(
                            cursor.getColumnIndex("longitude")));
                    imActivity.setLatitude(cursor.getDouble(
                            cursor.getColumnIndex("latitude")));
                    imActivity.setImages(stringToStringArray(cursor
                            .getString(cursor.getColumnIndex("images"))));
                    imActivity.setKind(cursor.getString(
                            cursor.getColumnIndex("kind")));
                    imActivity.setNumber(cursor.getInt(
                            cursor.getColumnIndex("number")));
                    imActivity.setCurrentTime(cursor.getInt(
                            cursor.getColumnIndex("current_time")));
                    imActivity.setOverTime(cursor.getString(
                            cursor.getColumnIndex("over_time")));
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return imActivity;
    }

    /**
     * 查询用户的参与的活动信息
     * @param user 查询条件用户
     * @return 该用户所对应参与的所有活动
     */
    public List<Join> queryMyJoin(User user){
        List<Join> joins = new ArrayList<>();
        List<String> activities = new ArrayList<>();
        Cursor cursor = db.query("Joins",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (cursor.getString(cursor.getColumnIndex
                        ("join_id")).equals(user.getObjectId())){
                    activities.add(cursor.getString(cursor
                            .getColumnIndex("activity_id")));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        for (String activity : activities) {
            Join join = new Join();
            join.setImActivity(queryImActivity(activity));
            join.setJoinUser(user);
            joins.add(join);
        }
        return joins;
    }

    /**
     * 查询用户参与的活动信息
     * @param user 对应用户
     * @return 该用户所对应参与的所有活动信息
     */
    public List<ImActivity> queryMyJoinImActivity(User user){
        List<ImActivity> imActivities = new ArrayList<>();
        List<String> activities = new ArrayList<>();
        Cursor cursor = db.query("Joins",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (cursor.getString(cursor.getColumnIndex
                        ("join_id")).equals(user.getObjectId())){
                    activities.add(cursor.getString(cursor
                            .getColumnIndex("activity_id")));
                }
            }while (cursor.moveToNext());
        }

        for (String activity : activities) {
            Log.i("TAG",activity);
            imActivities.add(queryImActivity(activity));
        }
        cursor.close();
        return imActivities;
    }

    /**
     * 删除当前用户的粉丝或者关注的人
     * @param isFollow 是否为关注类型的
     */
    public void deleteFollow(String uid,boolean isFollow){
        db.delete("User","user_id = ? and relevant_id = ? and is_follow = ? "
                , new String[]{ uid,BmobUser.getCurrentUser()
                .getObjectId() ,""+(isFollow ? 1: 0)});
    }

    /**
     * 添加一条图片存储地址信息
     * @param imageAddress 图片地址存储对象
     */
    public void addImageAddress(ImageAddress imageAddress){
        ContentValues values = new ContentValues();
        values.put("original_address",imageAddress.getOriginalAddress());
        values.put("compress_address",imageAddress.getCompressAddress());
        values.put("cloud_address",imageAddress.getCloudAddress());
        db.insert("Images",null , values);
    }

    /**
     * 根据图片的原始地址，查询该图片的所有存储地址信息
     * @param originalAddress 原始图片地址
     * @return   图片存储地址信息 返回null则为未查询到改图片信息
     */
    public ImageAddress queryImageAddress(String originalAddress){
        Cursor cursor = db.query("Images",null,"original_address = ?"
                ,new String[]{originalAddress},null,null,null,"1");
        if (cursor.moveToFirst()){
            ImageAddress address = new ImageAddress();
            address.setOriginalAddress(cursor.getString(
                    cursor.getColumnIndex("original_address")));
            address.setCompressAddress(cursor.getString(
                    cursor.getColumnIndex("compress_address")));
            address.setCloudAddress(cursor.getString(
                    cursor.getColumnIndex("cloud_address")));
            return address;
        }
        return null;

    }

    public ImageAddress queryImageAddressByCompressAddress(String compressAddress){
        Cursor cursor = db.query("Images",null,"compress_address = ?"
                ,new String[]{compressAddress},null,null,null,"1");
        if (cursor.moveToFirst()){
            ImageAddress address = new ImageAddress();
            address.setOriginalAddress(cursor.getString(
                    cursor.getColumnIndex("original_address")));
            address.setCompressAddress(cursor.getString(
                    cursor.getColumnIndex("compress_address")));
            address.setCloudAddress(cursor.getString(
                    cursor.getColumnIndex("cloud_address")));
            return address;
        }
        return null;
    }

    /**
     * 根据图片原始地址删除图片的信息记录
     * @param originalAddress 图片的原始地址
     */
    public void deleteImageAddress(String originalAddress){
        db.delete("Images","original_address = ?"
                ,new String[]{originalAddress});
    }

    /**
     * 更新Images信息表中图片存储地址
     * @param originalAddress 原始地址  作为更新的标识符
     * @param compressAddress 需更新图片压缩后的地址，如果为null则不更新
     * @param cloudAddress    需更新的图片云端地址，如果为null则不更新
     */
    public void updateImageAddress(String originalAddress
            , String compressAddress , String cloudAddress){
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(compressAddress)){
            values.put("compress_address",compressAddress);
        }
        if (!TextUtils.isEmpty(cloudAddress)){
            values.put("cloud_address",cloudAddress);
        }
        db.update("Images",values,"original_address = ?"
                ,new String[]{originalAddress});
    }

    public void updateImageAddress(String compressAddress , String cloudAddress){
        if (!TextUtils.isEmpty(cloudAddress)){
            ContentValues values = new ContentValues();
            values.put("cloud_address",cloudAddress);
            db.update("Images",values,"compress_address = ?",
                    new String[]{compressAddress});
        }
    }

}
