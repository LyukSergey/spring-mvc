package com.lss.l9springDataJpaCustom.annotation;

import com.lss.l9springDataJpaCustom.registrar.MyJpaRegistrar;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MyJpaRegistrar.class)
public @interface EnableMyJpa {

    String[] basePackages() default {};
}
