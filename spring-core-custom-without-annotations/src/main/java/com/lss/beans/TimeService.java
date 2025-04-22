package com.lss.beans;

import com.lss.ObjectTracker;

public class TimeService {

    public TimeService() {
        ObjectTracker.created(getClass());
    }

    public long now() {
        return System.currentTimeMillis();
    }

}
