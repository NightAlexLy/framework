package org.luisyang.framework.resource;

/**
 *  资源不存在异常
 * 
 *  @author LuisYang
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -670942006134302951L;

	public ResourceNotFoundException() {
		super("资源不存在");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	protected ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
