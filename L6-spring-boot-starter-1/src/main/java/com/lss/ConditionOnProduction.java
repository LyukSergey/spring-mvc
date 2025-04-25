package com.lss;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Conditional;

@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnProductionCondition.class)
public @interface ConditionOnProduction {

}
