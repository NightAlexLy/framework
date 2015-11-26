package org.luisyang.framework.service.exception;

/**
 * 服务未找到 
 */
public class ServiceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1568220468173000357L;

	public ServiceNotFoundException() {
		super("找不到对应服务");
	}

	public ServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceNotFoundException(String message) {
		super(message);
	}

	public ServiceNotFoundException(Throwable cause) {
		super(cause);
	}
}
