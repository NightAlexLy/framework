package org.luisyang.framework.http.servlet;

import javax.servlet.http.HttpServlet;

/**
 * 重写接口
 * 
 * @author LuisYang
 */
public abstract interface Rewriter {

	/**
	 * 获得后缀<br/>
	 * 	.htm
	 * @return
	 */
	public abstract String getSuffix();

	/**
	 * 获得视图的后缀<br/>
	 * 	.html
	 * @return
	 */
	public abstract String getViewSuffix();

	/**
	 * 根据路径和Servlet的名称获得完整路径
	 * @param contextPath  路径
	 * @param servletClass  Servlet名称
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getPathURI(String contextPath, Class<? extends HttpServlet> servletClass)
			throws ServletNotFoundException;

	/**
	 * 根据路径和Servlet的名称获得完整路径
	 * @param contextPath
	 * @param servletClass
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getURI(String contextPath, Class<? extends HttpServlet> servletClass)
			throws ServletNotFoundException;

	/**
	 * 根据路径和Servlet的名称获得完整路径
	 * @param contextPath
	 * @param servletClass
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getViewURI(String contextPath, Class<? extends HttpServlet> servletClass)
			throws ServletNotFoundException;

	/**
	 * 获得视图目录地址：<br/>
	 * 	/WEB-INFO/page
	 * @return
	 */
	public abstract String getViewRoot();

	/**
	 * 获得视图文件的后缀名：<br/>
	 * 	 .jsp
	 * @return
	 */
	public abstract String getViewFileSuffix();

	/**
	 * 包名是否匹配
	 * @param servletClass
	 * @return
	 */
	public abstract boolean isAccepted(Class<? extends HttpServlet> servletClass);

	/**
	 * 根据视图位置路径生成完整的路径
	 * @param servletClass  Servlet类名
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getViewFilePath(Class<? extends HttpServlet> servletClass) throws ServletNotFoundException;
}
