package org.luisyang.framework.http.servlet;

import javax.servlet.http.HttpServlet;

import org.luisyang.util.StringHelper;

/**
 * 包重写
 */
public class PackageRewriter implements Rewriter {
	
	protected final String rootPackage;

	/*
	 * 初始化
	 */
	public PackageRewriter(Package rootPackage) {
		this.rootPackage = rootPackage.getName();
	}

	/**
	 * 初始化
	 * @param rootPackageName
	 */
	public PackageRewriter(String rootPackageName) {
		this.rootPackage = rootPackageName;
	}

	protected String doGet(String contextPath, Class<? extends HttpServlet> servletClass, String subfix)
			throws ServletNotFoundException {
		String name = servletClass.getCanonicalName();
		if (name.startsWith(this.rootPackage)) {
			StringBuilder builder = new StringBuilder();
			if ((!StringHelper.isEmpty(contextPath)) && (!"/".equals(contextPath))) {
				builder.append(contextPath);
			}
			int lastIndex = name.lastIndexOf('.') + 1;
			for (int index = this.rootPackage.length(); index < name.length(); index++) {
				char ch = name.charAt(index);
				switch (ch) {
				case '.':
					builder.append('/');
					break;
				default:
					if (lastIndex == index) {
						builder.append(Character.toLowerCase(ch));
					} else {
						builder.append(ch);
					}
					break;
				}
			}
			builder.append(subfix);
			return builder.toString();
		}
		throw new ServletNotFoundException(String.format("重写规则不支持该Servlet:%s", new Object[] { name }));
	}

	public String getURI(String contextPath, Class<? extends HttpServlet> servletClass)
			throws ServletNotFoundException {
		return doGet(contextPath, servletClass, getSuffix());
	}

	public String getViewURI(String contextPath, Class<? extends HttpServlet> servletClass)
			throws ServletNotFoundException {
		return doGet(contextPath, servletClass, getViewSuffix());
	}

	public String getSuffix() {
		return ".htm";
	}

	public String getViewSuffix() {
		return ".html";
	}

	public String getViewRoot() {
		return "/WEB-INF/pages";
	}

	public String getViewFileSuffix() {
		return ".jsp";
	}

	public boolean isAccepted(Class<? extends HttpServlet> servletClass) {
		return servletClass == null ? false : servletClass.getCanonicalName().startsWith(this.rootPackage);
	}

	public String getPathURI(String contextPath, Class<? extends HttpServlet> servletClass)
			throws ServletNotFoundException {
		return doGet(contextPath, servletClass, "/");
	}

	public String getViewFilePath(Class<? extends HttpServlet> servletClass) throws ServletNotFoundException {
		return doGet(getViewRoot(), servletClass, getViewFileSuffix());
	}
}
