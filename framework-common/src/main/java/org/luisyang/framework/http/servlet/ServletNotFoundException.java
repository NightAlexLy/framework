package org.luisyang.framework.http.servlet;

/**
 * Servlet不存在
 * 
 * @author LuisYang
 */
public class ServletNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ServletNotFoundException() {
	}

	public ServletNotFoundException(String message) {
		super(message);
	}

	public ServletNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServletNotFoundException(Throwable cause) {
		super(cause);
	}

	protected ServletNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
