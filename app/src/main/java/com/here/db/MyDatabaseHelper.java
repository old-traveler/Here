package com.here.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hyc on 2017/9/16 14:37
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 建立用户表Sql语句
     */
    public static final String CREATE_USER = "create table IF NOT EXISTS User(user_id text," +
            "nickname text,sex text,age integer,birth text,introduction text,head text," +
            "address text,show_number boolean,show_age boolean,show_birth boolean,background" +
            " text,relevant_id text,tips text,is_follow INTEGER,primary key(user_id,relevant_id,is_follow))";
    /**
     * 建立即时活动信息表Sql语句
     */
    public static final String CREATE_IMACTIVITY = "create table if not exists ImActivity(" +
            "id text primary key,uid text,title text,describe text,is_apply boolean,location" +
            " text,longitude double,latitude double,images text,over_time text,kind text," +
            "number integer,current_time integer,publish_time text,publish_date text)";
    /**
     * 建立参与关系表Sql语句
     */
    public static final String CREATE_JOIN = "create table IF NOT EXISTS Joins(" +
            "join_id text,activity_id text,primary key(join_id,activity_id))";
    /**
     * 建立图片信息表Sql语句
     */
    public static final String CREATE_IMAGE = "create table IF NOT EXISTS Images(" +
            "original_address text primary key,compress_address text,cloud_address text)";


    public static final String CREATE_IGNORE_FIND = "create table IF NOT EXISTS " +
            "Ignore(owner text,find_id text,primary key(owner,find_id))";
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase
            .CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_IMACTIVITY);
        db.execSQL(CREATE_JOIN);
        db.execSQL(CREATE_IMAGE);
        db.execSQL(CREATE_IGNORE_FIND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
