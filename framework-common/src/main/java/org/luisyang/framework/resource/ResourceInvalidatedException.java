package org.luisyang.framework.resource;

/**
 * 资源已失效
 * 
 *  @author LuisYang
 */
public class ResourceInvalidatedException extends RuntimeException {
	private static final long serialVersionUID = 4958185414722624263L;

	public ResourceInvalidatedException() {
		super("资源已失效");
	}

	public ResourceInvalidatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceInvalidatedException(String message) {
		super(message);
	}

	public ResourceInvalidatedException(Throwable cause) {
		super(cause);
	}
}