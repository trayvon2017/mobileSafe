package com.example.dengdeng.mobilesafe.db.domain;

/**
 * Created by fbfatboy on 2018/4/18.
 */

public class Blacknum {
    private String phone;
    private int mode;

    public Blacknum() {
    }

    public Blacknum(String phone, int mode) {
        this.phone = phone;
        this.mode = mode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
