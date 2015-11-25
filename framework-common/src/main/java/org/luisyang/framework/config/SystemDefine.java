package org.luisyang.framework.config;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.http.servlet.Rewriter;
import org.luisyang.framework.http.session.authentication.AuthenticationException;
import org.luisyang.framework.http.session.authentication.PasswordAuthentication;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.service.ServiceSession;

/**
 * 系统定义
 * 
 * @author LuisYang
 */
public abstract class SystemDefine {

	/**
	 * 是否是严格的MVC
	 * @return
	 */
	public boolean isStrictMVC() {
		return false;
	}

	/**
	 * 获得全球唯一标识
	 * @return
	 */
	public abstract String getGUID();

	/**
	 * 系统名字
	 * @return
	 */
	public abstract String getSystemName();

	/**
	 * 系统描述
	 * @return
	 */
	public abstract String getSystemDescription();

	/**
	 * 获得重写信息
	 * @return
	 */
	public abstract Rewriter getRewriter();

	/**
	 * 调用登录获得用户ID
	 * @param session  会话
	 * @param accountName  用户名
	 * @param password 密码
	 * @return
	 * @throws AuthenticationException  认证异常
	 * @throws SQLException
	 */
	public abstract int readAccountId(ServiceSession session, String accountName, String password)
			throws AuthenticationException, SQLException;

	/**
	 * 写入登录日志
	 * @param request 请求
	 * @param response 响应
	 * @param serviceSession 会话
	 * @param authentication 认证信息
	 * @param accountId 用户ID
	 */
	public abstract void writeLog(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession,
			PasswordAuthentication authentication, int accountId);

	/**
	 * 获得数据供应商字符串 
	 * 	db.master.provider
	 * @param paramClass
	 * @return
	 */
	public abstract String getDataProvider(Class<? extends Resource> resourceType);

	/**
	 * 获得资源名称
	 * @param resourceType  资源类型。 类名
	 * @return
	 */
	public abstract String getSchemaName(Class<? extends Resource> resourceType);

	/**
	 * 获得网站域名
	 * @return
	 */
	public abstract String getSiteDomainKey();

	/**
	 * 获得网站首页地址
	 * @return
	 */
	public abstract String getSiteIndexKey();

	/**
	 * 错误最大时长
	 * @return
	 */
	public int getMaxErrorTimes() {
		return 10;
	}

	/**
	 * 验证码长度
	 * @return
	 */
	public int getVerifyCodeLength() {
		return 6;
	}
}
