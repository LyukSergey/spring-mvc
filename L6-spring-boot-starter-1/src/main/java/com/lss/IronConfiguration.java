package com.lss;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RavenProperties.class)
public class IronConfiguration {

    @Bean
    @ConditionOnProduction
//    @ConditionalOnProperty("raven.where")
    @ConditionalOnMissingBean
    @ConditionalOnRaven
    public RavenListener ravenListener(RavenProperties ravenProperties) {
        return new RavenListener(ravenProperties);
    }
}
