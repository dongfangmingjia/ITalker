package com.warner.common.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by warner on 2018/1/10.
 */

public class HashUtil {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String convertToHexStrng(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte a: b) {
            sb.append(HEX_DIGITS[(a & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[a & 0x0f]);
        }
        return sb.toString();
    }

    public static String getMD5String(String str) {
        MessageDigest md5;
        try{
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md5.update(str.getBytes());
        return convertToHexStrng(md5.digest());
    }


    public static String getMD5String(File file) {
        MessageDigest md5;
        try{
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream in = null;
        byte[] buffer = new byte[1024];
        int numRead;
        try {
            in = new FileInputStream(file);
            while ((numRead = in.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return convertToHexStrng(md5.digest());





        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
