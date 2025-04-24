package com.lss.beanPostProcessors;

import com.lss.annotations.InjectRandomInt;
import java.lang.reflect.Field;
import java.util.Random;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            final InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
            if (annotation != null) {
                final int min = annotation.min();
                final int max = annotation.max();
                Random random = new Random();
                int i = random.nextInt(max - min + 1) + min;
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, i);
            }

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
