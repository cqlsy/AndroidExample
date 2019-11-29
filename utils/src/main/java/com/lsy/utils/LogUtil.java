/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import timber.log.Timber;

/**
 * Log相关工具类
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    private static int LEVEL = VERBOSE;
    private static String GLOBAL_TAG = "global tag";

    /**
     * @param isDebug
     * @param globalTag
     * @param logLevel  打印的等级
     */
    public static void init(boolean isDebug, String globalTag, int logLevel) {
        GLOBAL_TAG = globalTag;
        LEVEL = logLevel;
        if (isDebug) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static void v(String msg) {
        v(GLOBAL_TAG, msg);
    }

    public static void d(String msg) {
        d(GLOBAL_TAG, msg);
    }

    public static void i(String msg) {
        i(GLOBAL_TAG, msg);
    }

    public static void w(String msg) {
        w(GLOBAL_TAG, msg);
    }

    public static void e(String msg) {
        e(GLOBAL_TAG, msg);
    }


    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Timber.v(tag + " =====>> " + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Timber.d(tag + " =====>> " + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Timber.i(tag + " =====>> " + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Timber.w(tag + " =====>> " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Timber.e(tag + " =====>> " + msg);
        }
    }
}