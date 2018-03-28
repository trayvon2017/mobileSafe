package com.example.dengdeng.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dengdeng on 2018/3/27.
 */

public class Md5Utils {
    public static String md5Code(String text){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodeDigest = md.digest(text.getBytes());
        StringBuffer sb =new StringBuffer();
        for(byte b :encodeDigest ){
            int temp = b&0xff;
            String tempHex = Integer.toHexString(temp);
            if(tempHex.length()<2){
                tempHex = "0"+tempHex;
            }
            sb.append(tempHex);

        }
        return sb.toString();
    }
}

