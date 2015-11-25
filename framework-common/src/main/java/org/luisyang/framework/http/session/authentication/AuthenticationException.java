package org.luisyang.framework.http.session.authentication;

/**
 * 认证异常 
 */
public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1310625378250296285L;

	public AuthenticationException() {
		super("未登录或会话超时,请重新登录");
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}
}
