package com.lss.l6springboot.listener;

import com.lss.RavenListener;
import com.lss.RavenProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MyRavenListener extends RavenListener {

    public MyRavenListener(RavenProperties ravenProperties) {
        super(ravenProperties);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("event = " + event);
    }
}
