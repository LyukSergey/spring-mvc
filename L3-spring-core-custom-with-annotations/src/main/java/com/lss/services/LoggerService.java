package com.lss.services;

import com.lss.ObjectTracker;
import com.lss.annotation.MyComponent;

@MyComponent
public class LoggerService {

    public LoggerService() {
        ObjectTracker.created(getClass());
    }

    public void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}
