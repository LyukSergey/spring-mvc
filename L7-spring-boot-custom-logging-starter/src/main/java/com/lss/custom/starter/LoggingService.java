package com.lss.custom.starter;

import com.lss.spingboot.custom.annotations.MyComponent;

@MyComponent
public class LoggingService {
    public void log(String msg) {
        System.out.println("[LOG]: " + msg);
    }
}
