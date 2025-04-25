package com.lss;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ProfileCheckerApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("🔥 ProfileCheckerApplicationContextInitializer called");
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        if (environment.getActiveProfiles().length == 0) {
            throw new RuntimeException("No active profiles found");
        }
    }
}
