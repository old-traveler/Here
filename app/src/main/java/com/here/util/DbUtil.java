package com.here.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.here.HereApplication;
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
    public void addFollow(User user , boolean isFollow){
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
        values.put("tips",addTip(user.getTips()));
        db.insert("User",null,values);
    }


    public String addTip(String[] tips){
        String tip = "";
        if (tips != null && tips.length > 0){
            for (int i = 0; i < tips.length; i++) {
                if (tips.length == 2){
                    return tips[0] + "-" + tips[1];
                }else {
                    if ((i == tips.length -1 || i == 0)){
                        tip = tip + tips[i];
                    }else {
                        tip = tip + "-" + tips[i];
                    }
                }
            }
        }
        Log.i("TAG","添加tip "+tip);
        return tip;
    }


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
                    user.setTips(queryTips(cursor.getString(
                            cursor.getColumnIndex("tips"))));
                    users.add(user);
                }while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return users;
    }


    public String[] queryTips(String tips){
        if (TextUtils.isEmpty(tips)){
            return null;
        }else {
            return tips.split("-");
        }
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
