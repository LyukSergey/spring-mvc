package com.lss.beans;

public class UserService {

    private final TimeService timeService;

    public UserService(TimeService timeService) {
        this.timeService = timeService;
    }

    public void printNow() {
        System.out.println("Current time: " + timeService.now());
    }

    public TimeService getTimeService() {
        return timeService;
    }

}
