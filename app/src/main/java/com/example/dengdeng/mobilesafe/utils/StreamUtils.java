package com.example.dengdeng.mobilesafe.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dengdeng on 2018/3/20.
 */

public class StreamUtils {

    public static String stream2String(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length=-1;
        try {
            while((length=is.read(buffer))!=-1){
                bos.write(buffer,0,length);
            }
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
