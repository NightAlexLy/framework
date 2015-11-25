package org.luisyang.framework.http.session.authentication;

/**
 * 拒绝访问异常
 * 
 * @author LuisYang
 */
public class AccesssDeniedException extends RuntimeException {
	
	private static final long serialVersionUID = 586999803946467795L;

	public AccesssDeniedException() {
		super("拒绝访问");
	}

	public AccesssDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccesssDeniedException(String message) {
		super(message);
	}

	public AccesssDeniedException(Throwable cause) {
		super(cause);
	}
}
