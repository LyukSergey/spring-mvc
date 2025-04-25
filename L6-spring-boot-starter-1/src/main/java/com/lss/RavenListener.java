package com.lss;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@RequiredArgsConstructor
public class RavenListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RavenProperties ravenProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ravenProperties.getWhere().forEach(where -> {
            System.out.printf("%n...Raven start fling to %s...%n", where);
        });

    }
}
