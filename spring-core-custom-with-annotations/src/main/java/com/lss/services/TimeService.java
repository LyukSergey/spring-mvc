package com.lss.services;

import com.lss.ObjectTracker;
import com.lss.annotation.MyComponent;
import com.lss.annotation.MyScope;
import com.lss.util.Scope;

@MyComponent
@MyScope(value = Scope.SINGLETON)
public class TimeService {

    public TimeService() {
        ObjectTracker.created(getClass());
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
