package com.lsy.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by adu on 2017/5/20.
 * 摘要生成工具类
 */

public class Digest {

    /**
     * 生成md5消息摘要
     *
     * @param s
     * @return
     */
    public static String md5(String s) {
        return hash(s, "MD5");
    }


    /**
     * 生成sha256消息摘要
     *
     * @param s
     * @return
     */
    public static String sha256(String s) {
        return hash(s, "SHA-256");
    }


    /**
     * 生成sha512消息摘要
     *
     * @param s
     * @return
     */
    public static String sha512(String s) {
        return hash(s, "SHA-512");
    }


    private static String hash(String s, String type) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            md.update(s.getBytes());
            byte[] bytes = md.digest();
            return result(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }


    private static String result(byte[] bytes) {
        int i;
        StringBuilder buf = new StringBuilder("");
        for (byte b : bytes) {
            i = b;
            if (i < 0) i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }
}
