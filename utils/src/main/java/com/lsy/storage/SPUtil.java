/*
 * Copyright (c) 2017 sh. All rights reserved.
 */

package com.lsy.storage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lsy.crypto.AES;
import com.lsy.crypto.Digest;
import com.lsy.crypto.KeyStoreUtil;

import java.util.UUID;


/**
 * SharedPreferences 相关工具类
 * 使用之前必须在  Application 中调用 {@link SPUtil#initKey(Application)}
 */
public class SPUtil {

    private static String mName = "black_boy";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences sharedPreferencesPub;

    private static final String CURRENT_USER_ID_KEY = "yee_af8Gif7Fi6";
    private static final String AES_PASSWORD_KEYSTORE_ALIAS = "yee_n4W2l3e6s8I";
    private static final String AES_PASSWORD_KEY = "yee_8F9H3b24iAc";
    private static final String AES_DEFAULT_PASSWORD = "7Ke0ITdLPnzR0a9o";

    private static String password;

    private static Application mApp;

    /**
     * 初始化加密key
     * 该key与设备有关，与账号无关
     */
    public static void initKey(Application app) {
        mApp = app;
        mName = mApp.getPackageName();
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // AndroidKeyStore not support in API 17
            // use default constant password
            password = AES_DEFAULT_PASSWORD;
            return;
        }*/
        String enKey = getStringPub(AES_PASSWORD_KEY, "");
        if (TextUtils.isEmpty(enKey)) {
            // 本地没有加密key,随机生成一个key
            password = Digest.md5(UUID.randomUUID().toString());
            // 得到加密后key
            enKey = KeyStoreUtil.getInstance().encrypt(password, AES_PASSWORD_KEYSTORE_ALIAS);
            if (TextUtils.isEmpty(enKey)) {
                // OS not support AndroidKeyStore
                // use default constant password
                enKey = Digest.md5(AES_DEFAULT_PASSWORD);
            }
            // 存储加密key到SharedPreferences
            putPub(AES_PASSWORD_KEY, enKey);
        } else {
            // 本地有加密key,解密得到真实key
            password = KeyStoreUtil.getInstance().decrypt(enKey, AES_PASSWORD_KEYSTORE_ALIAS);
            if (TextUtils.isEmpty(password)) {
                // if OS not support AndroidKeyStore
                // use default constant password
                password = AES_DEFAULT_PASSWORD;
            }
        }
    }

    private static SharedPreferences getInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = mApp.getSharedPreferences(mName + getCurUserId(), Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    private static SharedPreferences getInstancePub() {
        if (sharedPreferencesPub == null) {
            sharedPreferencesPub = mApp.getSharedPreferences(mName, Context.MODE_PRIVATE);
        }
        return sharedPreferencesPub;
    }

    /**
     * userId改变时调用
     * 重新初始化sharedPreferences
     */
    public static void changeUser(String newUserId) {
        if (TextUtils.isEmpty(newUserId)) {
            newUserId = "0123";
        } else {
            setCurUserId(newUserId);
        }
        sharedPreferences = mApp.getSharedPreferences(mName + newUserId, Context.MODE_PRIVATE);
    }

    /**
     * 清空指定userId的数据
     */
    public static void clear(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        SharedPreferences preferences = mApp.getSharedPreferences(mName + userId, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    /**
     * 清除当前保存的 user信息
     */
    public static void clear() {
        SharedPreferences preferences = mApp.getSharedPreferences(mName + getCurUserId(), Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    private static void setCurUserId(String userId) {
        SharedPreferences.Editor editor = getInstancePub().edit();
        editor.putString(CURRENT_USER_ID_KEY, encrypt(userId));
        editor.apply();
    }

    private static String getCurUserId() {
        String userId = getInstancePub().getString(CURRENT_USER_ID_KEY, "");
        if (TextUtils.isEmpty(userId)) {
            return "0123"; /* 默认无用户的存储 */
        }
        return decrypt(userId);
    }

    public static void put(String key, String value) {
        put(key, value, false);
    }

    public static void put(String key, int value) {
        put(key, String.valueOf(value), false);
    }

    public static void put(String key, long value) {
        put(key, String.valueOf(value), false);
    }

    public static void put(String key, float value) {
        put(key, String.valueOf(value), false);
    }

    public static void put(String key, boolean value) {
        put(key, String.valueOf(value), false);
    }

    public static void put(String key, double value) {
        put(key, String.valueOf(value), false);
    }

    public static void putPub(String key, String value) {
        put(key, String.valueOf(value), true);
    }

    public static void putPub(String key, int value) {
        put(key, String.valueOf(value), true);
    }

    public static void putPub(String key, long value) {
        put(key, String.valueOf(value), true);
    }

    public static void putPub(String key, float value) {
        put(key, String.valueOf(value), true);
    }

    public static void putPub(String key, boolean value) {
        put(key, String.valueOf(value), true);
    }

    public static void putPub(String key, double value) {
        put(key, String.valueOf(value), true);
    }


    //是否包含key
    public static boolean has(String key) {
        return getInstance().contains(key);
    }

    public static int getInt(String key, int defValue) {
        String str = getInstance().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Integer.parseInt(decrypt(str));
        }
    }

    public static double getDouble(String key, double defValue) {
        String str = getInstance().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Double.parseDouble(decrypt(str));
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        String str = getInstance().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Boolean.parseBoolean(decrypt(str));
        }
    }

    public static String getString(String key, String defValue) {
        String str = getInstance().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return decrypt(str);
        }
    }

    public static long getLong(String key, long defValue) {
        String str = getInstance().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Long.parseLong(decrypt(str));
        }
    }

    public static float getFloat(String key, float defValue) {
        String str = getInstance().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Float.parseFloat(decrypt(str));
        }
    }


    public static int getIntPub(String key, int defValue) {
        String str = getInstancePub().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Integer.parseInt(str);
        }
    }

    public static double getDoublePub(String key, double defValue) {
        String str = getInstancePub().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Double.parseDouble(str);
        }
    }

    public static boolean getBooleanPub(String key, boolean defValue) {
        String str = getInstancePub().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Boolean.parseBoolean(str);
        }
    }

    public static String getStringPub(String key, String defValue) {
        String str = getInstancePub().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return str;
        }
    }

    public static long getLongPub(String key, long defValue) {
        String str = getInstancePub().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Long.parseLong(str);
        }
    }

    public static float getFloatPub(String key, float defValue) {
        String str = getInstancePub().getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return defValue;
        } else {
            return Float.parseFloat(str);
        }
    }


    public static void remove(String... keys) {
        if (keys != null) {
            SharedPreferences.Editor editor = getInstance().edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }


    public static void removePub(String... keys) {
        if (keys != null) {
            SharedPreferences.Editor editor = getInstancePub().edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }

    /**
     * 存储数据到SharedPreferences
     *
     * @param isPub 是否是用户无关数据, 用户无关数据不需要加密
     */
    private static void put(String key, String value, boolean isPub) {
        SharedPreferences pref = isPub ? getInstancePub() : getInstance();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, isPub ? value : encrypt(value));
        editor.apply();
    }

    private static String encrypt(String value) {
        return AES.encrypt(value, password);
    }

    private static String decrypt(String value) {
        return AES.decrypt(value, password);
    }
}
