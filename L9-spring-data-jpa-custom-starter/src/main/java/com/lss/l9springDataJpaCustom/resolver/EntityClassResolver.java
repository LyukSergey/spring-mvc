package com.lss.l9springDataJpaCustom.resolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class EntityClassResolver {

    public static Class<?> resolve(Class<?> repoInterface) {
        for (Type iface : repoInterface.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType pt) {
                Class<?> raw = (Class<?>) pt.getRawType();
                if (raw.getSimpleName().equals("MyJpaRepository")) {
                    return (Class<?>) pt.getActualTypeArguments()[0];
                } else {
                    Class<?> candidate = resolve(raw);
                    if (candidate != null) {
                        return candidate;
                    }
                }
            } else if (iface instanceof Class<?> clz) {
                Class<?> candidate = resolve(clz);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }
}
