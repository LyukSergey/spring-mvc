
package com.lss;

import com.lss.custom.starter.LoggingService;
import com.lss.spingboot.custom.annotations.MyAutowired;
import com.lss.spingboot.custom.annotations.MyController;
import com.lss.spingboot.custom.annotations.MyGetMapping;

@MyController
public class HelloController {
    @MyAutowired
    private LoggingService logger;

    @MyGetMapping("/hello")
    public String hello() {
        logger.log("Викликано /hello");
        return "Привіт з мого фреймворку!";
    }
}
