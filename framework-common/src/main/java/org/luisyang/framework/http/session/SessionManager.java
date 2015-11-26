package org.luisyang.framework.http.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceProvider;

/**
 * 会话管理 
 *
 * @author LuisYang
 */
public abstract class SessionManager extends Resource implements AutoCloseable {
	
	public SessionManager(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 获得会话
	 * @param request
	 * @param response
	 * @return
	 */
	public abstract Session getSession(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 获得会话
	 * @param request
	 * @param response
	 * @param flag
	 * @return
	 */
	public abstract Session getSession(HttpServletRequest request,
			HttpServletResponse response, boolean flag);

	public final Class<? extends Resource> getIdentifiedType() {
		return SessionManager.class;
	}
}