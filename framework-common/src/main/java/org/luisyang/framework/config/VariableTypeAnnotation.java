package org.luisyang.framework.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 变量类型类注解
 * 
 */
@Documented
@Inherited
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface VariableTypeAnnotation {
	/**
	 * 唯一标识
	 * 
	 * @return
	 */
	String id();

	/**
	 * 名称
	 * 
	 * @return
	 */
	String name();
}
