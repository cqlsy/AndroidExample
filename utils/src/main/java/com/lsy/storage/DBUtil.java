/*
 * Copyright (c) 2017 sh. All rights reserved.
 */

package com.lsy.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lsy.crypto.Digest;
import com.lsy.utils.LogUtil;

/**
 * 这个类目前暂时用作 数据存储，使用 key - value 来存储数据
 */
public class DBUtil extends SQLiteOpenHelper {

    private static final String TAG = DBUtil.class.getSimpleName();

    private static DBUtil mInstance;
    private static SQLiteDatabase mDatabase = null;

    public static DBUtil getInstance() {
        if (mInstance == null || mDatabase == null) {
            throw new RuntimeException("you must init First before you want to use DBUtil");
        }
        return mInstance;
    }

    public static void init(Context context, int version) {
        if (mInstance == null) {
            String dbName = context.getPackageName();
            mInstance = new DBUtil(context, dbName, null, version);
        }
        if (mDatabase == null) {
            mDatabase = mInstance.getWritableDatabase();
        }
    }

    private DBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /**
     * 应用创建时执行一次，应用替换时不执行
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * 应用更新是执行
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //更新表操作

        }
        //Toast.makeText(mContext, "onUpgrade", Toast.LENGTH_SHORT).show();
        //升级使用的模板
        /*switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2:
                db.execSQL("alter table Book add column category_id integer");
            default:
        }*/
    }

    private final String mNetTableName = "NET_TABLE";

    /**
     * 存储Json至指定数据表
     *
     * @param jsonString 要存储的数据
     */
    public void saveNetDataDatabase(String url, String jsonString) {
        ensureTable(mNetTableName, "params", "content");
        // 删除旧数据
        String paramsString = Digest.md5(JSON.toJSONString(url));
        mDatabase.delete(mNetTableName, "params = ?", new String[]{paramsString});
        ContentValues values = new ContentValues();
        values.put("params", paramsString);
        values.put("content", jsonString);
        mDatabase.insert(mNetTableName, null, values);
    }

    /**
     * 从数据库读取指定的Json数据
     *
     * @return
     */
    public String getNetDataFromDatabase(String url) {
        ensureTable(mNetTableName, "params", "content");
        String paramsString = Digest.md5(JSON.toJSONString(url));
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(mNetTableName);
        builder.append(" WHERE params = '");
        builder.append(paramsString);
        builder.append("'");
        LogUtil.d(TAG, "db query：" + builder.toString());
        String jsonString = "";
        try {
            Cursor cursor = mDatabase.rawQuery(builder.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    jsonString = cursor.getString(cursor.getColumnIndex("content"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            mDatabase.delete(mNetTableName, null, null);
        }
        return jsonString;
    }

    /**
     * @param tableName 数据表名
     * @param key       数据中的唯一标识 , 我们需要保证这个key 的唯一性
     * @param str       要存储的数据
     */
    public void saveStringToDatabase(String tableName, String key, String str) {

        ensureTable(tableName, "params", "content");
        // 删除旧数据
        String paramsString = Digest.md5(key);
        mDatabase.delete(tableName, "params = ?", new String[]{paramsString});
        ContentValues values = new ContentValues();
        values.put("params", paramsString);
        values.put("content", str);
        mDatabase.insert(tableName, null, values);
    }

    /**
     * 从数据库读取指定的Json数据
     *
     * @param tableName 数据库表名
     * @param key       数据中的唯一标识 , 我们需要保证这个key 的唯一性
     * @return str
     */
    public String getStringFromDatabase(String tableName, String key) {

        ensureTable(tableName, "params", "content");
        String paramsString = Digest.md5(key);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(tableName);
        builder.append(" WHERE params = '");
        builder.append(paramsString);
        builder.append("'");
        LogUtil.d(TAG, "db query：" + builder.toString());
        String str = "";
        try {
            Cursor cursor = mDatabase.rawQuery(builder.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    str = cursor.getString(cursor.getColumnIndex("content"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            mDatabase.delete(tableName, null, null);
        }
        return str;
    }

    /**
     * 清空某个表
     *
     * @param tableName 表名
     */
    public void clearTable(String tableName) {
        mDatabase.delete(tableName, "id >= ?", new String[]{"0"});
    }

    /**
     * 判断某张表是否存在
     * 不存在时创建该表
     *
     * @param tableName 表名
     * @param keys      列名，仅存储String类型
     */
    private void ensureTable(@NonNull String tableName, String... keys) {
        boolean isExist = false;
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = mDatabase.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    isExist = true;
                }
            }
            cursor.close();
        } catch (Exception ignore) {
        }
        // 不存在时创建表
        if (!isExist) {
            StringBuilder builder = new StringBuilder();
            builder.append("create table ").append(tableName).append(" (");
            for (String key : keys) {
                builder.append(" ").append(key).append(" text,");
            }
            builder.append("content" + " text" + " )");
            LogUtil.d(TAG, "db create：" + builder.toString());
            mDatabase.execSQL(builder.toString());
        }
    }

    public void createTableOnNoTable(String... tableNames) {
        if (tableNames != null && tableNames.length != 0) {
            for (String s : tableNames) {
                ensureTable(s, "params");
            }
        }
    }
}