package org.luisyang.framework.service.exception;

/**
 * 参数异常
 */
public class ParameterException extends RuntimeException {
	private static final long serialVersionUID = 1568220468173000357L;

	public ParameterException() {
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}
}