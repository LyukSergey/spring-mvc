package com.lss.beans;

import com.lss.ObjectTracker;

public class UserService {

    private final TimeService timeService;

    public UserService(TimeService timeService) {
        this.timeService = timeService;
        ObjectTracker.created(getClass());
    }

    public void printNow() {
        System.out.println("Current time: " + timeService.now());
    }

    public TimeService getTimeService() {
        return timeService;
    }

}
