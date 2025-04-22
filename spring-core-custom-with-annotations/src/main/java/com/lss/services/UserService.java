package com.lss.services;

import com.lss.ObjectTracker;
import com.lss.annotation.MyAutowired;
import com.lss.annotation.MyComponent;
import com.lss.annotation.MyScope;
import com.lss.util.Scope;

@MyComponent
@MyScope(value = Scope.SINGLETON)
public class UserService {

    @MyAutowired
    private LoggerService logger;

    @MyAutowired
    private TimeService timeService;

    public UserService() {
        ObjectTracker.created(getClass());
    }

    public void doWork() {
        logger.log("Doing work at " + timeService.now());
    }
}
