package com.lss.beanDefinishins;

import com.lss.Scope;
import java.util.function.Supplier;

public class BeanDefinition {

    private final String name;
    private final Class<?> type;
    private final Supplier<?> creator;
    private final Scope scope;

    public BeanDefinition(String name, Class<?> type, Supplier<?> creator, Scope scope) {
        this.name = name;
        this.type = type;
        this.creator = creator;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Supplier<?> getCreator() {
        return creator;
    }

    public Scope getScope() {
        return scope;
    }

}
