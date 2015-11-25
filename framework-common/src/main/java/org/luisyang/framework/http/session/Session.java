package org.luisyang.framework.http.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.http.session.authentication.AccesssDeniedException;
import org.luisyang.framework.http.session.authentication.AuthenticationException;
import org.luisyang.framework.http.session.authentication.PasswordAuthentication;
import org.luisyang.framework.http.session.authentication.VerifyCodeAuthentication;

import com.alibaba.druid.sql.visitor.functions.Right;

public abstract interface Session {

	/**
	 * 获得令牌
	 * @return
	 */
	public abstract String getToken();

	/**
	 * 获得远程IP
	 * @return
	 */
	public abstract String getRemoteIP();

	/**
	 * 是否认证
	 * @return
	 */
	public abstract boolean isAuthenticated();

	/**
	 * 获得用户ID
	 * @return
	 * @throws AuthenticationException
	 */
	public abstract int getAccountId() throws AuthenticationException;

	/**
	 * 获得属性值
	 * @param param 属性名
	 * @return
	 */
	public abstract String getAttribute(String key);

	/**
	 * 设置属性值
	 * @param key  主键
	 * @param value 值
	 */
	public abstract void setAttribute(String key, String value);

	/**
	 * 删除属性值
	 * @param key
	 * @return
	 */
	public abstract String removeAttribute(String key);

	/**
	 * 获得验证码
	 * @param key
	 * @return
	 */
	public abstract String getVerifyCode(String key);

	/**
	 * 
	 * @param paramString
	 * @param paramVerifyCodeGenerator
	 * @return
	 */
	public abstract String getVerifyCode(String paramString, VerifyCodeGenerator );

	public abstract void invalidVerifyCode(String paramString);

	public abstract void authenticateVerifyCode(VerifyCodeAuthentication paramVerifyCodeAuthentication)
			throws AuthenticationException;

	public abstract int authenticatePassword(PasswordAuthentication paramPasswordAuthentication)
			throws AuthenticationException;

	public abstract int checkIn(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, PasswordAuthentication paramPasswordAuthentication)
					throws AuthenticationException;

	public abstract void tryAccessResource(String paramString) throws AccesssDeniedException;

	public abstract boolean isAccessableResource(String paramString);

	public abstract boolean isAccessableResource(Class<?> paramClass);

	public abstract boolean isAccessableResource(Right paramRight);

	public abstract void invalidate(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse);
}
