package com.github.bluesgao.jsm.example;

import com.github.bluesgao.jsm.annotation.MonitorMethod;

public class UserService {
    @MonitorMethod
    public String getUserName(String uid) {
        try {
            double time = 1000 * Math.random();
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "gx";
    }

    @MonitorMethod
    public void setUserName(String userName) {
        try {
            double time = 1000 * Math.random();
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUserName2(String userName) {
        try {
            double time = 1000 * Math.random();
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getUserName2(String uid) {
        try {
            double time = 1000 * Math.random();
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "gx";
    }
}
