package org.luisyang.framework.service.exception;

/**
 * 逻辑异常
 */
public class LogicalException extends RuntimeException {
	private static final long serialVersionUID = 1568220468173000357L;

	public LogicalException() {
	}

	public LogicalException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogicalException(String message) {
		super(message);
	}

	public LogicalException(Throwable cause) {
		super(cause);
	}
}