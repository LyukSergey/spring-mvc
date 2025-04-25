package com.lss;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class OnRavenConditional extends AllNestedConditions {

    public OnRavenConditional() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty("raven.where")
    public static class R {

    }

    @ConditionalOnProperty(value = "raven.on", havingValue = "true")
    public static class C {

    }
}
