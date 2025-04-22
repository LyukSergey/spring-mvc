package com.lss.context;

import com.lss.annotation.MyAutowired;
import com.lss.annotation.MyComponent;
import com.lss.annotation.MyScope;
import com.lss.util.Scope;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyApplicationContext {

    private final Map<Class<?>, Object> singletonBeans = new HashMap<>();
    private final Map<Class<?>, Scope> beanScopes = new HashMap<>();

    public MyApplicationContext(String basePackage) {
        try {
            List<Class<?>> classes = scanPackage(basePackage);
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(MyComponent.class)) {
                    Scope scope = Scope.SINGLETON;
                    if (clazz.isAnnotationPresent(MyScope.class)) {
                        scope = clazz.getAnnotation(MyScope.class).value();
                    }
                    beanScopes.put(clazz, scope);
                    if (scope == Scope.SINGLETON) {
                        Object instance = createBean(clazz);
                        singletonBeans.put(clazz, instance);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        try {
            Scope scope = beanScopes.get(clazz);
            if (scope == Scope.SINGLETON) {
                return (T) singletonBeans.get(clazz);
            } else {
                return (T) createBean(clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createBean(Class<?> clazz) throws Exception {
        Object instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                field.setAccessible(true);
                Object dependency = getBean(field.getType());
                field.set(instance, dependency);
            }
        }

        return instance;
    }

    private List<Class<?>> scanPackage(String basePackage) throws Exception {
        String path = basePackage.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        File directory = new File(resource.toURI());
        List<Class<?>> classes = new ArrayList<>();

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }

    public void printBeans() {
        System.out.println("🟦 Singleton Beans:");
        singletonBeans.forEach((clazz, obj) ->
                System.out.printf("  [%s] → %s%n", clazz.getSimpleName(), obj.getClass().getSimpleName()));

        System.out.println("\n🟨 Bean Scopes:");
        beanScopes.forEach((clazz, scope) ->
                System.out.printf("  [%s] → %s%n", clazz.getSimpleName(), scope));
    }
}
