/*
 * Copyright (c) 2017 sh. All rights reserved.
 */

package com.lsy.crypto;

import android.annotation.TargetApi;
import android.security.KeyPairGeneratorSpec;
import android.text.TextUtils;
import android.util.Base64;

import com.lsy.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * Created by adu on 2017/4/10.
 */

@TargetApi(18)
public class KeyStoreUtil {

    private KeyStore mKeyStore;

    private static class InstanceHolder {
        private static final KeyStoreUtil instance = new KeyStoreUtil();
    }

    private KeyStoreUtil() {
    }

    public static KeyStoreUtil getInstance() {
        return InstanceHolder.instance;
    }

    private void initKeyStore(String alias) {
        try {
            String keyStoreType = "AndroidKeyStore";
            try {
                mKeyStore = KeyStore.getInstance(keyStoreType);
            } catch (Exception ignore) {
                // maybe throw NoSuchAlgorithmException
                // such as YunOS
                keyStoreType = KeyStore.getDefaultType();
                mKeyStore = KeyStore.getInstance(keyStoreType);
            }
            mKeyStore.load(null);

            // Create new key if needed
            if (!mKeyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 99);
                KeyPairGeneratorSpec spec = null;
                spec = new KeyPairGeneratorSpec.Builder(Utils.getInstance().getContext())
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", keyStoreType);
                generator.initialize(spec);
                KeyPair keyPair = generator.generateKeyPair();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加密方法
     *
     * @param plainText 　需要加密的字符串
     * @param alias     　加密秘钥
     * @return
     */
    public String encrypt(String plainText, String alias) {
        String result = "";
        if (TextUtils.isEmpty(plainText) || TextUtils.isEmpty(alias)) {
            return result;
        }
        initKeyStore(alias);
        byte[] vals = null;
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry =
                    (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);
            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(plainText.getBytes("UTF-8"));
            cipherOutputStream.close();
            vals = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (vals == null) {
            // OS unsupported KeyStore encrypt
            return "";
        }
        return Base64.encodeToString(vals, Base64.DEFAULT);
    }


    /**
     * 解密方法
     *
     * @param cipherText 密文
     * @param alias      解密密钥
     * @return
     */
    public String decrypt(String cipherText, String alias) {
        String result = "";
        if (TextUtils.isEmpty(cipherText) || TextUtils.isEmpty(alias)) {
            return result;
        }
        initKeyStore(alias);
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry =
                    (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }
            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i);
            }
            result = new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
