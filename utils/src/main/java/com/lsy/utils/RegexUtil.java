/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adu on 2017/5/20.
 * 正则相关工具类
 */

public class RegexUtil {

    /**
     * 登录密码6-16位,简单匹配
     */
//    public static final String REGEX_SIMPLE_PWD = "^[0-9A-Za-z]{6,16}$";
    public static final String REGEX_SIMPLE_PWD = "^[0-9A-Za-z\\\\d_\\\\.\\\\-\\\\*@#]{6,20}$";

    /**
     * 登录密码6-16位，精确匹配
     */
//    public static final String REGEX_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
    public static final String REGEX_PWD = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,20}$";

    /**
     * 姓名
     */
    public static final String NAME = "^[\\u4e00-\\u9fa5]{2,6}$";

    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE = "^1([38]\\d|4[579]|5[0-35-9]|7[0135-8])\\d{8}$";

    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 正则：URL
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";

    /**
     * 正则：银行卡
     */
    public static final String BANK_CARD_NO = "^(\\d{16}|\\d{19})$";

    /**
     * 正则
     */
    public static final String NOT_NULL_CHAR = "(^([0-9]+$)|(^[a-zA-Z]+$)|(^[0-9A-Za-z]+$)|[\\u4e00-\\u9fa5]+$)";

    /**
     * 手机号或者邮箱
     */
    public static final String REGEX_EMAIL_OR_PHONE = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$|^1([38]\\d|4[579]|5[0-35-9]|7[0135-8])\\d{8}$";


    public static final String REGEX_INTERNATIONAL_PHONE = "[0-9]{5,30}";

    public static boolean isInternationalPhoneNum(String mobile) {
        Pattern p = Pattern.compile(REGEX_INTERNATIONAL_PHONE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 判断手机号是否合法
     *
     * @param mobile
     * @return
     */
    public static boolean isPhoneNum(String mobile) {
        Pattern p = Pattern.compile("1[3|5|7|8|][0-9]{9}");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isEmailOrPhone(String string) {
        if (null == string || "".equals(string)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*|1[3|5|7|8|][0-9]{9}");//复杂匹配
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public static boolean isPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) return false;
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    /**
     * 判断btc地址是否合法
     *
     * @param btcAddress
     * @return
     */
    public static boolean isBtcAddress(String btcAddress) {
        if (null == btcAddress || "".equals(btcAddress) || btcAddress.length() < 20) return false;
        Pattern p = Pattern.compile("^[1,3][A-Za-z0-9]+$");
        Matcher m = p.matcher(btcAddress);
        return m.matches();
    }

    /**
     * 判断eth地址是否合法
     *
     * @param ethAddress
     * @return
     */
    public static boolean isEthAddress(String ethAddress) {
        if (null == ethAddress || "".equals(ethAddress) || ethAddress.length() < 40) return false;
        Pattern p = Pattern.compile("^0x[A-Za-z0-9]{40}+$");
        Matcher m = p.matcher(ethAddress);
        return m.matches();
    }

    public static boolean isUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Pattern p = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        Matcher m = p.matcher(url);
        return m.matches();
    }

    public static boolean isIdCard(String card) {
        if (TextUtils.isEmpty(card)) {
            return false;
        }
        Pattern p = Pattern.compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
        Matcher m = p.matcher(card);
        return m.matches();
    }

    /**
     * 判断cny金额是否可输入
     *
     * @param str
     * @return
     */
    public static boolean cnyCanEdit(String str) {
        if (str.equals("")) {
            return true;
        }
        if (str.length() > 20) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(([1-9]\\d{0,30})|0)(\\.\\d{0,2})?$");
        if (!pattern.matcher(str).matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检验字符串是否符合规则
     *
     * @return
     */
    public static boolean regStr(String str, String regex) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
