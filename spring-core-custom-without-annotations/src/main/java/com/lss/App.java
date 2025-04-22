package com.lss;

import com.lss.beans.TimeService;
import com.lss.beans.UserService;
import com.lss.context.AppContext;

public class App {

    public static void main(String[] args) {
        AppContext context = new AppContext();

        // Реєструємо TimeService як singleton
        context.register("timeService", TimeService.class, TimeService::new, Scope.SINGLETON);

        // Реєструємо два різні UserService (один prototype, інший singleton)
        context.register("userService1", UserService.class,
                () -> new UserService((TimeService) context.getBean("timeService")),
                Scope.PROTOTYPE);

        context.register("userService2", UserService.class,
                () -> new UserService((TimeService) context.getBean("timeService")),
                Scope.SINGLETON);

        UserService u1 = (UserService) context.getBean("userService1");
        UserService u2 = (UserService) context.getBean("userService1");
        System.out.println("Prototype? u1 == u2 → " + (u1 == u2)); // false

        UserService u3 = (UserService) context.getBean("userService2");
        UserService u4 = (UserService) context.getBean("userService2");
        System.out.println("Singleton? u3 == u4 → " + (u3 == u4)); // true

        System.out.println("Same TimeService? " + (u3.getTimeService() == u1.getTimeService())); // true
    }
}

