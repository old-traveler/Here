package com.here.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
import com.here.bean.ImActivity;
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
     * 添加一条关注或者粉丝记录
     * @param user   用户信息
     * @param isFollow 是关注还是粉丝
     */
    public void addFollow(User user , boolean isFollow ){
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
        db.insert("User",null,values);
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
            for (int i = 0; i < tips.length; i++) {
                if (tips.length == 2){
                    return tips[0] + "&" + tips[1];
                }else {
                    if ((i == tips.length -1 || i == 0)){
                        tip = tip + tips[i];
                    }else {
                        tip = tip + "&" + tips[i];
                    }
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
        Log.i("TAG","插入"+db.insert("Joins",null,values) + (join.getImActivity().getObjectId() == null));
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
     * @return 该用户所应用参与的所有活动
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
                    Log.i("TAG",cursor.getString(cursor
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
    public void deleteFollow(boolean isFollow){
        db.delete("User","relevant_id = ?  and is_follow = ?"
                , new String[]{ BmobUser.getCurrentUser()
                .getObjectId() , ""+(isFollow ? 1: 0)});
    }

}
