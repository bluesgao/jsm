package com.github.bluesgao.jsm.example;

import com.github.bluesgao.jsm.annotation.MonitorClass;

@MonitorClass
public class UserService {
    public String getUserName(String uid) {
        try {
            double time = 1000 * Math.random();
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "gx";
    }

    public void setUserName(String userName) {
        try {
            double time = 1000 * Math.random();
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
