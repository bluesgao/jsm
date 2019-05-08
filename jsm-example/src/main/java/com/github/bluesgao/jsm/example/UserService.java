package com.github.bluesgao.jsm.example;

import com.github.bluesgao.jsm.annotation.MonitorClass;

@MonitorClass
public class UserService {
    public String getUserName(String uid) {
        return "gx";
    }

    public void setUserName(String userName) {
    }
}
