/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

/**
 * 工具类全局 (context ==> Application) 实例提供
 * 当使用 {@link ToastUtil} 必须实例化
 */
public class Utils {

    private Application mApp;

    private static class InstanceHolder {
        private static final Utils instance = new Utils();
    }

    private Utils() {
    }

    public static Utils getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Application application) {
        mApp = application;
    }

    public Context getContext() {
        if (mApp == null) {
            throw new RuntimeException("should init first!");
        }
        return mApp;
    }

    /**
     * 比较版本号
     *
     * @param oldVersionName 1.0.1
     * @param newVersionName 1.0.3
     * @return If newVersionName(1.0.3) 大于 oldVersionName(1.0.1), return true.
     * If newVersionName(1.0.1) 小于等于 oldVersionName(1.0.1), return false.
     */
    public static boolean versionCompare(String oldVersionName, String newVersionName) {
        if (TextUtils.isEmpty(oldVersionName) || TextUtils.isEmpty(newVersionName)) {
            return false;
        }
        String[] oldVersion = oldVersionName.split(".");
        String[] newVersion = newVersionName.split(".");
        try {
            int length = Math.min(oldVersion.length, newVersion.length);
            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(newVersion[i]) > Integer.parseInt(oldVersion[i])) {
                    return true;
                } else if (Integer.parseInt(newVersion[i]) < Integer.parseInt(oldVersion[i])) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return newVersion.length > oldVersion.length;
    }


}
