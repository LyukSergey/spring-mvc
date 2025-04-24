package com.lss;

import com.lss.beans.HelloWorld;
import com.lss.beans.HelloWorldInterface;
import com.lss.beans.Quoter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        exampleOfGettingBean(context, false);
        profilingExample(context, false);
        context.getBean(Quoter.class).sayQuote();
    }

    private static void profilingExample(ApplicationContext context, boolean turnOn) throws InterruptedException {
        while (turnOn) {
            Thread.sleep(100);
            context.getBean(Quoter.class).sayQuote();
        }
    }

    private static void exampleOfGettingBean(ApplicationContext context, boolean turnOn) {
        if (turnOn) {
            HelloWorld obj1 = (HelloWorld) context.getBean("helloWorld");
            HelloWorld obj2 = context.getBean(HelloWorld.class);
            HelloWorld obj3 = (HelloWorld) context.getBean(HelloWorldInterface.class, "helloWorld");
            obj3.getMessage();
        }
    }
}
