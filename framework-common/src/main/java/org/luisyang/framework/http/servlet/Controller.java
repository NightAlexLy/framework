package org.luisyang.framework.http.servlet;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.resource.PromptLevel;
import org.luisyang.framework.resource.Prompter;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.util.StringHelper;
import org.luisyang.util.parser.IntegerParser;

/**
 * 控制器
 * 
 * @author KQL LuisYang
 */
public abstract class Controller extends Resource {
	
	protected static String REMOTE_AGENT_HEADER = "x-agent";
	protected static String REMOTE_ADDRESS_HEADER = "x-for";
	protected static String SCHEME_HEADER = "x-scheme";
	protected static String SERVER_HEADER = "x-server";
	protected static String PORT_HEADER = "x-port";

	public Controller(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 设置类名唯一标识
	 */
	public Class<? extends Resource> getIdentifiedType() {
		return Controller.class;
	}

	/**
	 * 写入主机信息<br/>
	 * <p>如：localhost:8080</p> 
	 * @param request 请求 
	 * @param defaultPort 端口号
	 * @param builder 字符串
	 */
	protected void writeHostName(HttpServletRequest request, int defaultPort, StringBuilder builder) {
		String serverName = request.getHeader(SERVER_HEADER);
		if (StringHelper.isEmpty(serverName)) {
			serverName = request.getServerName();
		}
		int port = IntegerParser.parse(request.getHeader(PORT_HEADER), 0);
		if (port == 0) {
			port = request.getServerPort();
		}
		if (port == defaultPort) {
			builder.append(serverName);
		} else {
			builder.append(serverName);
			builder.append(':').append(Integer.toString(port));
		}
	}

	/**
	 * 获得主机名称
	 * @param request
	 * @return
	 */
	public String getHostName(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		writeHostName(request, isSecure(request) ? 443 : 80, builder);
		return builder.toString();
	}

	/**
	 * 获得远程代理信息<br/>
	 * 用户代理 User Agent，是指浏览器,它的信息包括硬件平台、系统软件、应用软件和用户个人偏好<br/>
	 * <p>如：Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11</p>
	 * @param request 请求
	 * @return
	 */
	public String getRemoteAgent(HttpServletRequest request) {
		String remoteAgent = request.getHeader(REMOTE_AGENT_HEADER);
		if (StringHelper.isEmpty(remoteAgent)) {
			remoteAgent = request.getHeader("User-Agent");
		}
		return remoteAgent;
	}

	/**
	 * 获得远程器请求地址
	 * @param request 请求
	 * @return
	 */
	public String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader(REMOTE_ADDRESS_HEADER);
		if (StringHelper.isEmpty(remoteAddr)) {
			remoteAddr = request.getRemoteAddr();
		}
		return remoteAddr;
	}

	/**
	 * 是否安全 <br/>
	 * <p> 1表示https:// 0表示http://</p>
	 * @param request 请求
	 * @return
	 */
	public boolean isSecure(HttpServletRequest request) {
		return (request.isSecure()) || ("1".equals(request.getHeader(SCHEME_HEADER)));
	}

	/**
	 * 获得静态路径
	 * @param request
	 * @param paramVarArgs
	 * @return
	 */
	public abstract String getStaticPath(HttpServletRequest request, String... paramVarArgs);

	/**
	 * 获得重定向路径
	 * @param request 请求
	 * @param message
	 * @return
	 */
	public abstract String getRedirect(HttpServletRequest request, String uri);

	/**
	 * 获得统一资源标识符
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @return
	 * @throws ServletNotFoundException 不存在
	 */
	public abstract String getURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException;

	/**
	 * 根据视图获得统一资源标识符
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @return
	 * @throws ServletNotFoundException 不存在
	 */
	public abstract String getViewURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException;

	/**
	 * 根据分页获得统一资源标识符
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getPagingURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException;

	/**
	 * 根据分页子项获得统一资源标识符
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @param id 元素ID
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getPagingItemURI(HttpServletRequest request,
			Class<? extends Servlet> servletClass, long id) throws ServletNotFoundException;

	/**
	 * 根据分页子项获得统一资源标识符
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @param id 元素ID
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getPagingItemURI(HttpServletRequest request,
			Class<? extends Servlet> servletClass, int id) throws ServletNotFoundException;

	/**
	 * 根据分页子项获得统一资源标识符
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @param id 元素ID
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getPagingItemURI(HttpServletRequest request,
			Class<? extends Servlet> servletClass, String id) throws ServletNotFoundException;

	/**
	 * 根据Servlet信息获得跳转地址信息
	 * @param request 请求
	 * @param servletClass Servlet类名
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getForwardURI(HttpServletRequest request,
			Class<? extends Servlet> servletClass) throws ServletNotFoundException;

	/**
	 * 根据视图信息获得跳转地址信息
	 * @param request 请求
	 * @param servletClass Serlvet
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getViewForwardURI(HttpServletRequest request,
			Class<? extends Servlet> servletClass) throws ServletNotFoundException;

	/**
	 * 重定向
	 * @param request  请求
	 * @param response 响应
	 * @param location 地址
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String location) throws ServletException, IOException;

	/**
	 * 跳转
	 * @param request 请求
	 * @param response 响应
	 * @param location 地址
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void forward(HttpServletRequest request,
			HttpServletResponse response, String location) throws ServletException, IOException;

	/**
	 * 跳转控制器
	 * @param request  请求
	 * @param response 响应
	 * @param controller  控制器  继承HttpServlet
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void forwardController(HttpServletRequest request,
			HttpServletResponse response, Class<? extends HttpServlet> controller)
					throws ServletException, IOException;

	/**
	 * 跳转视图
	 * @param request  请求
	 * @param response 响应
	 * @param controller  控制器
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void forwardView(HttpServletRequest request,
			HttpServletResponse response, Class<? extends HttpServlet> controller)
					throws ServletException, IOException;

	/**
	 * 获得提示
	 * @param request 请求
	 * @return
	 */
	public abstract Prompter getPrompter(HttpServletRequest request);

	/**
	 * 匹配提示
	 * @param request  请求
	 * @param response 响应
	 * @param level 提示等级
	 * @return
	 */
	public abstract boolean hasPrompts(HttpServletRequest request,
			HttpServletResponse response, PromptLevel level);

	/**
	 * 获得提示内容
	 * @param request 请求
	 * @param response 响应
	 * @param level 提示等级
	 * @return
	 */
	public abstract String getPrompt(HttpServletRequest request,
			HttpServletResponse response, PromptLevel level);

	/**
	 * 提示
	 * @param request  请求
	 * @param response 响应
	 * @param level 提示等级
	 * @param message 消息
	 */
	public abstract void prompt(HttpServletRequest request,
			HttpServletResponse response, PromptLevel level, String message);

	/**
	 * 清空指定等级的提示
	 * @param request 请求
	 * @param response 响应
	 * @param level 提示等级
	 */
	public abstract void clearPrompts(HttpServletRequest request,
			HttpServletResponse response, PromptLevel level);

	/**
	 * 清空所有提示
	 * @param request
	 * @param response
	 */
	public abstract void clearAll(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 初始化
	 * @param servletContext
	 * @param rewriter
	 * @param servlets  
	 */
	public abstract void initialize(ServletContext servletContext, Rewriter rewriter,
			Set<Class<? extends HttpServlet>> servlets);

	/**
	 * 获得视图文件路径
	 * @param request
	 * @return
	 * @throws ServletNotFoundException
	 */
	public abstract String getViewFile(HttpServletRequest request) throws ServletNotFoundException;

	/**
	 * 重定向到登陆
	 * @param request 请求
	 * @param response 响应
	 * @param message 消息
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void redirectLogin(HttpServletRequest request,
			HttpServletResponse response, String message) throws ServletException, IOException;
}
