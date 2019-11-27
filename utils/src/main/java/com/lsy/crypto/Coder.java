package com.lsy.crypto;


/**
 * Created by adu on 2017/5/20.
 * 编解码工具类
 */

public class Coder {

    public static class Base64 {

        /**
         * base64编码
         *
         * @param data
         * @return
         */
        public static String encode(byte[] data) {
            return android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP);
        }


        /**
         * base64解码
         *
         * @param s
         * @return
         */
        public static byte[] decode(String s) {
            return android.util.Base64.decode(s, android.util.Base64.NO_WRAP);
        }
    }
}