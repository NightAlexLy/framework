package org.luisyang.framework.http.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.http.servlet.annotation.Right;
import org.luisyang.framework.http.session.authentication.AccesssDeniedException;
import org.luisyang.framework.http.session.authentication.AuthenticationException;
import org.luisyang.framework.http.session.authentication.PasswordAuthentication;
import org.luisyang.framework.http.session.authentication.VerifyCodeAuthentication;


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
	 * 获得验证码
	 * @param key
	 * @param generator  验证码生成器
	 * @return
	 */
	public abstract String getVerifyCode(String key, VerifyCodeGenerator generator);

	/**
	 * 验证验证码
	 * @param paramString
	 */
	public abstract void invalidVerifyCode(String code);

	/**
	 * 认证验证码
	 * @param authentication 验证码验证模型
	 * @throws AuthenticationException
	 */
	public abstract void authenticateVerifyCode(VerifyCodeAuthentication authentication)
			throws AuthenticationException;

	/**
	 * 认证密码
	 * @param authentication 密码认证模型
	 * @return
	 * @throws AuthenticationException
	 */
	public abstract int authenticatePassword(PasswordAuthentication authentication)
			throws AuthenticationException;

	/**
	 * 登录检查
	 * @param request  请求
	 * @param response  响应
	 * @param authentication  密码认证模型
	 * @return
	 * @throws AuthenticationException
	 */
	public abstract int checkIn(HttpServletRequest request,
			HttpServletResponse response, PasswordAuthentication authentication)
					throws AuthenticationException;

	/**
	 * 验证资源
	 * @param resource
	 * @throws AccesssDeniedException
	 */
	public abstract void tryAccessResource(String resource) throws AccesssDeniedException;

	/**
	 * 是否为可使用资源
	 * @param paramString
	 * @return
	 */
	public abstract boolean isAccessableResource(String resource);
	
	/**
	 * 是否为可使用资源
	 * @param paramString
	 * @return
	 */
	public abstract boolean isAccessableResource(Class<?> clazz);
	/**
	 * 是否为可使用资源
	 * @param paramString
	 * @return
	 */
	public abstract boolean isAccessableResource(Right right);

	/**
	 * 设置无效时间
	 * @param request
	 * @param response
	 */
	public abstract void invalidate(HttpServletRequest request,
			HttpServletResponse response);
}
