package com.lss.logger;

import com.lss.utils.ObjectTracker;

public class Logger {

    public Logger() {
        ObjectTracker.created(getClass());
    }

    public void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}
