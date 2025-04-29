package com.lss.spingboot.custom;

import com.lss.spingboot.custom.annotations.MyAutowired;
import com.lss.spingboot.custom.annotations.MyComponent;
import com.lss.spingboot.custom.annotations.MyController;
import com.lss.spingboot.custom.annotations.MyGetMapping;
import com.lss.spingboot.custom.web.MyDispatcherServlet;
import com.lss.spingboot.custom.web.RouteRegistry;
import jakarta.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class MyApplication {

    public static void run(Class<?> mainClass) {
        String rootPackage = mainClass.getPackageName();

        Map<Class<?>, Object> beans = new HashMap<>();
        List<Class<?>> allClasses = scanClasses(rootPackage);
        allClasses.addAll(loadFromMetaInf());

        for (Class<?> clazz : allClasses) {
            if (clazz.isAnnotationPresent(MyComponent.class) || clazz.isAnnotationPresent(MyController.class)) {
                try {
                    beans.put(clazz, clazz.getDeclaredConstructor().newInstance());
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(MyAutowired.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(bean, beans.get(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        RouteRegistry registry = new RouteRegistry();
        for (Object bean : beans.values()) {
            Class<?> clazz = bean.getClass();
            if (clazz.isAnnotationPresent(MyController.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(MyGetMapping.class)) {
                        registry.register(method.getAnnotation(MyGetMapping.class).value(), bean, method);
                    }
                }
            }
        }

        final Connector connector = new Connector();
        connector.setPort(8080);
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        tomcat.getService().addConnector(connector);
        File myBaseDir = new File("./my-temp-tomcat-folder");
        if (!myBaseDir.exists()) {
            myBaseDir.mkdirs(); // створюємо папку, якщо її немає
        }
        Context ctx = tomcat.addContext("", myBaseDir.getAbsolutePath());
        HttpServlet dispatcher = new MyDispatcherServlet(registry);

        Class servletClass = MyDispatcherServlet.class;
        Tomcat.addServlet(
                ctx, servletClass.getSimpleName(), servletClass.getName());
        ctx.addServletMappingDecoded(
                "/*", servletClass.getSimpleName());

        Wrapper servlet = ctx.createWrapper();
        servlet.setName("dispatcher");
        servlet.setLoadOnStartup(1);
        servlet.setServlet(dispatcher);

        ctx.addChild(servlet);
        ctx.addServletMappingDecoded("/*", "dispatcher");

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }

    private static List<Class<?>> scanClasses(String pkg) {
        List<Class<?>> classes = new ArrayList<>();
        String path = pkg.replace('.', '/');
        Enumeration<URL> resources = null;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (resources.hasMoreElements()) {
            URL res = resources.nextElement();
            File dir = null;
            try {
                dir = new File(res.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            for (File file : dir.listFiles(f -> f.getName().endsWith(".class"))) {
                String className = pkg + "." + file.getName().replace(".class", "");
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> loadFromMetaInf()  {
        List<Class<?>> result = new ArrayList<>();
        Enumeration<URL> resources = null;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/my-framework.components");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (resources.hasMoreElements()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resources.nextElement().openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.add(Class.forName(line.trim()));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

}
