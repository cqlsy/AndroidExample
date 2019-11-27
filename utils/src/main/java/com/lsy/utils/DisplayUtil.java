/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * UI界面相关工具类
 */
public class DisplayUtil {
    /**
     * 强制切换语言
     */
    public static void forceLocale(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
//        config.locale = Locale.SIMPLIFIED_CHINESE;
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    /**
     * 得到屏幕高度
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 得到屏幕宽度
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 将px值转换为dp或dp值，保证尺寸大小不变
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将dp值转换为px值，保证尺寸大小不变
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * 计算TextView中文本预期占用的高度
     */
    public static int measureWidth(String text, Paint paint) {
        int width = 0;
        if (text != null && text.length() > 0) {
            int len = text.length();
            float[] widths = new float[len];
            paint.getTextWidths(text, widths);
            for (int j = 0; j < len; j++) {
                width += (int) Math.ceil(widths[j]);
            }
        }
        return width;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return StatusBar's height.
     */
    public static int getStatusBarHeight(Context context) {
        int height = dp2px(context, 20f); // default value
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int dimenResId = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(dimenResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.height = height;
            view.setLayoutParams(params);
        }
    }

    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    public static void measureView(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
