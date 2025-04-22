package com.lss.services;

import com.lss.ObjectTracker;
import com.lss.annotation.MyComponent;
import com.lss.annotation.MyScope;
import com.lss.util.Scope;

@MyComponent
@MyScope(value = Scope.SINGLETON)
public class LoggerService {

    public LoggerService() {
        ObjectTracker.created(getClass());
    }

    public void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}
