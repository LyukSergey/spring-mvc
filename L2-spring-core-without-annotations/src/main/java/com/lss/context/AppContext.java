package com.lss.context;

import com.lss.beanDefinishins.BeanDefinition;
import com.lss.Scope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AppContext {
    private final Map<String, BeanDefinition> beanDefinitionsByName = new HashMap<>();
    private final Map<Class<?>, List<BeanDefinition>> beanDefinitionsByType = new HashMap<>();
    private final Map<String, Object> singletonBeans = new HashMap<>();

    public <T> void register(String name, Class<T> type, Supplier<T> creator, Scope scope) {
        BeanDefinition def = new BeanDefinition(name, type, creator, scope);
        beanDefinitionsByName.put(name, def);
        beanDefinitionsByType.computeIfAbsent(type, k -> new ArrayList<>()).add(def);
    }

    public Object getBean(String name) {
        BeanDefinition def = beanDefinitionsByName.get(name);
        if (def == null) throw new RuntimeException("Bean not found by name: " + name);
        return getOrCreateBean(def);
    }

    public <T> T getBean(Class<T> type) {
        List<BeanDefinition> defs = beanDefinitionsByType.get(type);
        if (defs == null || defs.isEmpty()) throw new RuntimeException("Bean not found by type: " + type);
        if (defs.size() > 1) throw new RuntimeException("Multiple beans found for type: " + type);
        return (T) getOrCreateBean(defs.get(0));
    }

    private Object getOrCreateBean(BeanDefinition def) {
        if (def.getScope() == Scope.SINGLETON) {
            return singletonBeans.computeIfAbsent(def.getName(), n -> def.getCreator().get());
        } else {
            return def.getCreator().get();
        }
    }
}
