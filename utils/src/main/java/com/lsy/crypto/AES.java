package com.lsy.crypto;

import android.text.TextUtils;

import com.lsy.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by adu on 2017/5/20.
 * AES加解密
 */

public class AES {

    private static final String TAG = "AES";

    /**
     * 使用算法
     */
    private static final String SIGN_ALGORITHMS = "SHA-512";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";


    /**
     * Generates SHA512 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA512 of the password
     */
    private static SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance(SIGN_ALGORITHMS);
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] hash = digest.digest();
        return new SecretKeySpec(Arrays.copyOfRange(hash, 0, 32), KEY_ALGORITHM);
    }


    /**
     * AES加密
     *
     * @param data 数据
     * @param key  密钥
     * @return 加密后的数据，加密错误返回null
     */
    public static String encrypt(String data, String key) {
        if (TextUtils.isEmpty(data)) {
            return data;
        }
        String result;
        try {
            SecretKeySpec spec = generateKey(key);
            // 初始化为加密模式的密码器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv = new byte[16];
            new Random().nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(iv));
            byte[] byteContent = data.getBytes();
            byte[] resultBytes = cipher.doFinal(byteContent);

            byte[] resultArray = new byte[resultBytes.length + 16];
            System.arraycopy(iv, 0, resultArray, 0, 16);
            System.arraycopy(resultBytes, 0, resultArray, 16, resultBytes.length);

            result = Coder.Base64.encode(resultArray);// 加密
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES解密
     *
     * @param data 数据
     * @param key  密钥
     * @return 解密后的数据，解密错误返回null
     */
    public static String decrypt(String data, String key) {
        if (TextUtils.isEmpty(data)) {
            return data;
        }
        String result;
        try {
            SecretKeySpec spec = generateKey(key);
            byte[] iv = new byte[16];
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 创建密码器
            byte[] bytes = Coder.Base64.decode(data);
            System.arraycopy(bytes, 0, iv, 0, 16);
            byte[] dataBytes = new byte[bytes.length - 16];
            System.arraycopy(bytes, 16, dataBytes, 0, bytes.length - 16);

            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(iv));// 初始化为解密模式的密码器
            result = new String(cipher.doFinal(dataBytes));
            return result; // 明文
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.i(TAG, "AES decrypt failed! \n " +
                "the AES key is " + key + "\n" +
                "the data is " + data);
        return null;
    }
}
