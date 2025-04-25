package com.lss;

import com.lss.context.MyApplicationContext;
import com.lss.services.UserService;

/**
 * Hello world!
 *
 */
public class SpringCreWithAnnotations
{
    public static void main( String[] args )
    {
        MyApplicationContext context = new MyApplicationContext("com.lss.services");
        UserService userService = context.getBean(UserService.class);
        userService.doWork();
        ObjectTracker.print();
        context.printBeans();
    }
}
