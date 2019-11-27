/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by adu on 2017/5/20.
 * Toast相关工具类
 */

public class ToastUtil {
    /**
     * 之前显示的内容
     */
    private static int oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;

    public static void showToast(@StringRes int resId) {
        if (toast == null) {
            toast = Toast.makeText(Utils.getInstance().getContext(), resId, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (resId == oldMsg) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = resId;
                toast.setText(resId);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(String text) {
        showToast(Utils.getInstance().getContext(), Toast.LENGTH_SHORT, text);
    }

    public static void showLongToast(@StringRes int resId) {
        showToast(Utils.getInstance().getContext(), Toast.LENGTH_LONG, resId);
    }

    public static void showLongToast(String text) {
        showToast(Utils.getInstance().getContext(), Toast.LENGTH_LONG, text);
    }

    public static void showToast(Context ctx, @StringRes int resId) {
        showToast(ctx, Toast.LENGTH_SHORT, resId);
    }

    public static void showToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_SHORT, text);
    }


    public static void showLongToast(Context ctx, @StringRes int resId) {
        showToast(ctx, Toast.LENGTH_LONG, resId);
    }

    public static void showLongToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_LONG, text);
    }

    public static void showToast(Context ctx, int duration, int resId) {
        showToast(ctx, duration, ctx.getString(resId));
    }

    public static void showToast(Context ctx, int duration, String text) {
        Toast.makeText(ctx, text, duration).show();
    }

    /**
     * 在UI线程运行弹出
     */
    public static void showToastOnUiThread(final Activity ctx, final String text) {
        if (ctx != null) {
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showToast(ctx, text);
                }
            });
        }
    }

}
