package org.luisyang.framework.http.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块
 */
@Documented
@Inherited
@Target({java.lang.annotation.ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Module
{
  String id();
  
  String name();
  
  String description() default "";
}
