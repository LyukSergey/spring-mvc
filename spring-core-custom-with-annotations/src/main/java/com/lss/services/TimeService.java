package com.lss.services;

import com.lss.ObjectTracker;
import com.lss.annotation.MyAutowired;
import com.lss.annotation.MyComponent;

@MyComponent
public class TimeService {
    @MyAutowired
    private LoggerService logger;

    public TimeService() {
        ObjectTracker.created(getClass());
    }

    public long now() {
        logger.log("Doing work at TimeService = " + System.currentTimeMillis());
        return System.currentTimeMillis();
    }
}
