package com.lss;

import com.lss.beans.Quoter;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

public class PropertyFileApplicationContext extends GenericApplicationContext {

    public PropertyFileApplicationContext(String fileName) {
        final PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(this);
        final int beanDefinitions = reader.loadBeanDefinitions(fileName);
        System.out.println("found " + beanDefinitions + " bean definitions");
        refresh();
    }

    public static void main(String[] args) {
        final PropertyFileApplicationContext context = new PropertyFileApplicationContext("context.properties");
        context.getBean(Quoter.class).sayQuote();
    }
}
