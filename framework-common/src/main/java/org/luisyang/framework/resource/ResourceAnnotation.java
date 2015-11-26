package org.luisyang.framework.resource;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源注解
 */
@Documented
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAnnotation {
	ResourceRetention[]value() default { ResourceRetention.DEVELOMENT, ResourceRetention.PRE_PRODUCTION,
			ResourceRetention.PRODUCTION };
}
