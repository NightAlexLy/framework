package org.luisyang.framework.http.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.http.HttpServlet;

/**
 * 分页Servlet
 */
@Documented
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PagingServlet {
	boolean viewPaging() default true;

	boolean viewItem() default true;

	String currentToken() default "paging.current";

	String itemToken() default "id";

	Class<? extends HttpServlet>itemServlet() default HttpServlet.class;
}