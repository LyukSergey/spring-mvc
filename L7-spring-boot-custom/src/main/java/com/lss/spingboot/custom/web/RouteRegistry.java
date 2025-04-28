package com.lss.spingboot.custom.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RouteRegistry {

    private final Map<String, HandlerMethod> routes = new HashMap<>();

    public void register(String path, Object controller, Method method) {
        routes.put(path, new HandlerMethod(controller, method));
    }

    public HandlerMethod getHandler(String path) {
        return routes.get(path);
    }

    public static class HandlerMethod {

        public final Object controller;
        public final Method method;

        public HandlerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }
    }
}
