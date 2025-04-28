package com.lss.spingboot.custom.core;

import com.lss.spingboot.custom.annotations.MyComponent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MyContext {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void loadFromMetaInf() throws Exception {
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/my-framework.components");
        while (resources.hasMoreElements()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resources.nextElement().openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Class<?> clazz = Class.forName(line.trim());
                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        beans.put(clazz, instance);
                    }
                }
            }
        }
    }

    public <T> T getBean(Class<T> type) {
        return type.cast(beans.get(type));
    }

    public Collection<Object> getAllBeans() {
        return beans.values();
    }
}
