/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Created by adu on 2017/5/20.
 * 文本处理相关工具类
 */

public class TextUtil {

    public static String format(@NonNull String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    //将int类型转换为字符串
    public static String toString(int num) {
        return "" + num;
    }

    //将double类型转换为字符串
    public static String toString(double num) {
        return "" + num;
    }

    //将long类型转换为字符串
    public static String toString(long num) {
        return "" + num;
    }

    public static String splitInteger(String num) {
        if (!num.contains(".")) {
            return num;
        }

        String[] arr = num.split("[.]");
        return arr[0];
    }

    public static String splitInteger(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        String num = bigDecimal.toPlainString();
        return splitInteger(num);
    }

    public static String splitDecimal(String num) {
        if (!num.contains(".")) {
            return "";
        }

        String[] arr = num.split("[.]");
        if (arr.length == 2) {
            return "." + arr[1];
        } else {
            return "";
        }
    }

    public static String splitDecimal(Double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        String num = bigDecimal.toPlainString();
        return splitDecimal(num);
    }

    public static double getDouble(double value, int scale) {
        BigDecimal bg = new BigDecimal(value);
        double d = bg.setScale(scale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return d;
    }

    public static double getDouble(String value) {
        double d;
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        try {
            d = Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            d = 0;
        }
        if (Double.isNaN(d)) {
            return 0;
        }
        return d;
    }

    public static float getFloat(String value) {
        float d;
        try {
            d = Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
            d = 0;
        }
        return d;
    }

    public static long getLong(String value) {
        long d;
        try {
            d = Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            d = 0;
        }
        return d;
    }

    public static double getDouble(String value, int scale) {
        return getDouble(getDouble(value), scale);
    }

    public static boolean isFill(String... strs) {
        boolean isFill = true;
        for (String s : strs) {
            if (TextUtils.isEmpty(s)) {
                isFill = false;
                break;
            }
        }
        return isFill;
    }

    //比较字符串double值
    public static boolean doubleCompare(String one, String two) {
        double oneT;
        double twoT;
        if (TextUtils.isEmpty(one)) {
            one = "0";
        }
        if (TextUtils.isEmpty(two)) {
            two = "0";
        }
        oneT = Double.parseDouble(one);
        twoT = Double.parseDouble(two);
        return oneT > twoT;
    }

    public static String formatNum(double num, int scale) {
        StringBuilder builder = new StringBuilder("0.");
        for (int i = 0; i < scale; i++) {
            builder.append("0");
        }
        return new DecimalFormat(builder.toString()).format(num);
    }

    /**
     * 格式化千分的金额
     *
     * @param num 数值类型字符串
     * @return 格式化结果
     */
    public static String formatThousands(String num) {
        double v;
        try {
            v = Double.parseDouble(num);
        } catch (Exception e) {
            v = 0;
        }
        return formatThousands(v);
    }

    /**
     * 千分位格式化金额
     *
     * @param num 金额数值
     * @return 格式化后金额
     */
    public static String formatThousands(double num) {
        return new DecimalFormat("###,##0.00").format(num);
    }

    /**
     * 千分位格式化
     *
     * @param num double类型数值
     * @param d   格式化小数精度
     * @return 格式化结果
     */
    public static String formatThousands(double num, int d) {
        StringBuilder builder = new StringBuilder("###,##0");
        if (d > 0) {
            builder.append(".");
            for (int i = 0; i < d; i++) {
                builder.append("0");
            }
        }
        return new DecimalFormat(builder.toString()).format(num);
    }

    /**
     * 千分位格式化
     *
     * @param num double类型数值
     * @return 格式化结果
     */
    public static String formatThousandsOnly(double num, int d) {

        String sss = formatePrice(num, d);
//        if (sss.contains(".")) {
//            while (true) {
//                if (sss.endsWith("0")) {
//                    sss = sss.substring(0, sss.length() - 1);
//                } else {
//                    break;
//                }
//            }
        // 保证有两位小数
//            if (sss.endsWith(".")) {
//                sss = sss + "00";
//            }
//            if (sss.indexOf(".") == sss.length() - 2) {
//                sss = sss + "0";
//            }
//        }

        return sss;
    }

    /**
     * @param price 需要格式化的字符串（double类型的字符串）
     * @param scale 精度
     * @return
     */
    public static String formatePrice(String price, int scale) {
        // 首先进行千分格式化
        double d;
        try {
            d = Double.parseDouble(price);
        } catch (Exception e) {
            d = 0;
        }
        String s = TextUtil.formatThousands(d, scale);
        // 首先判断是否是以0结尾
//        while (s.endsWith("0")) {
//            s = s.substring(0, s.length() - 1);
//        }
//        // 最后在判断是否是以 . 结尾
//        if (s.endsWith(".")) {
//            s = s.substring(0, s.length() - 1);
//        }
//        if (s.endsWith("0")) {
//            s = "--";
//        }
        return s;
    }

    public static String formatePrice(double price, int scale) {
        // 首先进行千分格式化
        String s = TextUtil.formatThousands(price, scale);
        // 首先判断是否是以0结尾
//        while (s.endsWith("0")) {
//            s = s.substring(0, s.length() - 1);
//        }
//        // 最后在判断是否是以 . 结尾
//        if (s.endsWith(".")) {
//            s = s.substring(0, s.length() - 1);
//        }
//        if (s.endsWith("0")) {
//            s = "--";
//        }
        return s;
    }


    public static String formatDecimal(double cny, int scale) {
        String format = "0.0";
        for (int i = 1; i < scale; i++) {
            format = format + "0";
        }
        return new DecimalFormat(format).format(cny);
    }

    public static String getPercent(String s) {
        if (TextUtils.isEmpty(s)) {
            return "0.00%";
        }
        return "%";
    }

    public static String[] formatVol(String vol) {
        return formatVol(getDouble(vol));
    }

    /**
     * 格式化成交量
     *
     * @param vol 成交量
     * @return
     */
    public static String[] formatVol(double vol) {
        String result = "--";
        String unit = "";
        double m = vol;
        if (m == 0) {
            result = "0.00";
        }
        // 一手为100，计算手为单位时，此处应为100
        m = m / 1;
        if (Math.abs(m) >= 1000000000000L) {
            result = "" + formatNum(m / 1000000000000L, 2);
            unit = "万亿";
            return new String[]{result, unit};
        }

        if (Math.abs(m) >= 100000000) {
            result = "" + formatNum(m / 100000000, 2);
            unit = "亿";
            return new String[]{result, unit};
        }
        if (Math.abs(m) >= 10000) {
            result = "" + formatNum(m / 10000, 2);
            unit = "万";
            return new String[]{result, unit};
        }
        if (Math.abs(m) > 0) {
            if ((m % 1) != 0) {
                result = "" + formatNum(m / 1, 2);
            } else {
                result = "" + (int) m;
            }
        }
        return new String[]{result, unit};
    }

    /**
     * 格式化成交量
     *
     * @param vol 成交量
     * @return
     */
    public static String[] formatVolEn(double vol) {
        String result = "--";
        String unit = "";
        double m = vol;
        if (m == 0) {
            result = "0.00";
        }
        // 一手为100，计算手为单位时，此处应为100
        m = m / 1;
        if (Math.abs(m) >= 1000000000000L) {
            result = "" + formatNum(m / 1000000000000L, 2);
            unit = "T";
            return new String[]{result, unit};
        }

        if (Math.abs(m) >= 1000000000) {
            result = "" + formatNum(m / 1000000000, 2);
            unit = "B";
            return new String[]{result, unit};
        }
        if (Math.abs(m) >= 1000000) {
            result = "" + formatNum(m / 1000000, 2);
            unit = "M";
            return new String[]{result, unit};
        }
        if (Math.abs(m) >= 1000) {
            result = "" + formatNum(m / 1000, 2);
            unit = "K";
            return new String[]{result, unit};
        }
        if (Math.abs(m) > 0) {
            if ((m % 1) != 0) {
                result = "" + formatNum(m / 1, 2);
            } else {
                result = "" + (int) m;
            }
        }
        return new String[]{result, unit};
    }


    /**
     * 根据姓名和性别返回包装后显示的名字
     *
     * @param name
     * @param sex
     * @return
     */
    public static String replaceName(String name, String sex) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        String str = name.substring(0, 1);
        if (TextUtils.isEmpty(sex)) {
            for (int i = 0; i < name.length() - 1; i++) {
                str = str.concat("*");
            }
            return str;
        }
        if (sex.equals("男")) {
            str = str.concat("先生");
        } else {
            str = str.concat("女士");
        }
        return str;
    }


    /**
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username'chenziwen^password'1234
     */
    public static String mapToString(HashMap map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }

    /**
     * 方法名称:transStringToMap
     * 传入参数:mapString 形如 username'chenziwen^password'1234
     * 返回值:Map
     */
    public static HashMap stringToMap(String mapString) {
        HashMap map = new HashMap();
        StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "'");
        return map;
    }


    public static String getStringFromAssets(Context context, String name) {
        String encode = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[2048];
        int len = 0;
        try {
            // 默认以utf-8形式
            encode = "utf-8";
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len, encode));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readAssestTextFile(Context context, String assest) {
        String str = "";
        try {
            InputStream is;
            is = context.getAssets().open(assest);
            str = getStrFromStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String readAssestTextFile(Context context, int rawId) {
        String str = "";
        try {
            InputStream is;
            is = context.getResources().openRawResource(rawId);
            str = getStrFromStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String getStrFromStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    /**
     * 获取url中指定的参数
     *
     * @param url
     * @param key
     * @return
     */
    public static String getParam(String url, String key) {
        String[] tmp = url.split("[?]");
        String paramStr = "";
        String[] paramArr;
        if (tmp.length > 1) {
            paramStr = tmp[1];
        }
        if (TextUtils.isEmpty(paramStr)) {
            return "";
        } else {
            paramArr = paramStr.split("[&]");
            for (int i = 0; i < paramArr.length; i++) {
                String[] kvArr = paramArr[i].split("[=]");
                if (kvArr.length > 1 && kvArr[0].equals(key)) {
                    return kvArr[1];
                }
            }
            return "";
        }
    }

    public static String[] getParams(String origin, String addr) {
        String tmp = addr.replace(origin, "");
        if (TextUtils.isEmpty(tmp)) {
            return null;
        }
        String[] paramArr = tmp.split("/");
        return paramArr;
    }

    /**
     * 比较两个字符串  主要对str1 进行了判空  主要用于xml  中
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if (str2 == null) {
            throw new RuntimeException("str2 can not null value");
        }
        return str1 != null && str1.equals(str2);
    }


    /**
     * 对map的key按字母排序后，取value拼接
     *
     * @param map
     * @return
     */
    public static String sortValues(Map<String, ?> map) {
        if (map == null) {
            return "";
        }
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = keyList.size(); i < len; i++) {
            builder.append(map.get(keyList.get(i)).toString());
        }
        return builder.toString();
    }
}
