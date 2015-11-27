package org.luisyang.framework.http.achieve.servlet;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.http.achieve.servlet.filestore.FileStoreServlet;
import org.luisyang.framework.http.achieve.servlet.paging.PagingControllerServlet;
import org.luisyang.framework.http.achieve.servlet.view.DefaultViewController;
import org.luisyang.framework.http.achieve.servlet.view.PrdViewController;
import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.servlet.Rewriter;
import org.luisyang.framework.http.servlet.ServletNotFoundException;
import org.luisyang.framework.http.servlet.annotation.PagingServlet;
import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.http.session.SessionManager;
import org.luisyang.framework.http.upload.FileStore;
import org.luisyang.framework.resource.PromptLevel;
import org.luisyang.framework.resource.Prompter;
import org.luisyang.framework.resource.ResourceAnnotation;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRetention;
import org.luisyang.util.StringHelper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ResourceAnnotation
public class DefaultController extends Controller {
	protected static final String VIEW_URI_PARAMETER_NAME = "DIMENG_VIEW_URI";
	protected static final String VIEW_FILE_PARAMETER_NAME = "DIMENG_VIEW_FILE_URI";
	protected static final String URI_PARAMETER_NAME = "DIMENG_SERVLET_URI";
	protected static final String PAGING_PARAMETER_NAME = "DIMENG_PAGING_URI";
	protected static final String VIEW_FORWARD_URI_PARAMETER_NAME = "DIMENG_VIEW_FORWARD_URI";
	protected static final String URI_FORWARD_PARAMETER_NAME = "DIMENG_SERVLET_FORWARD_URI";
	protected static final String STATIC_RESOURCE_UUID = UUID.randomUUID().toString();
	protected static final String PROMPT_UUID = UUID.randomUUID().toString();
	private final Map<String, String> cachedPathes = new HashMap();
	private final Map<String, String> cachedRevertPathes = new HashMap();
	protected boolean initilized = false;
	protected String contextURI = "";

	public DefaultController(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	public String getStaticPath(HttpServletRequest request, String... staticPathes) {
		if ((isSecure(request)) || (staticPathes == null) || (staticPathes.length == 0)) {
			String contextPath = request.getContextPath();
			if (("/".equals(contextPath)) || (contextPath == null)) {
				contextPath = "";
			}
			return contextPath;
		}
		Object object = request.getAttribute(STATIC_RESOURCE_UUID);
		AtomicInteger times = null;
		if ((object == null) || (!(object instanceof AtomicInteger))) {
			times = new AtomicInteger(0);
			request.setAttribute(STATIC_RESOURCE_UUID, times);
		} else {
			times = (AtomicInteger) object;
		}
		return staticPathes[(times.getAndIncrement() % staticPathes.length)];
	}

	public String getRedirect(HttpServletRequest request, String uri) {
		try {
			URI to = new URI(uri);
			if (to.isAbsolute()) {
				return uri;
			}
			if (isSecure(request)) {
				StringBuilder builder = new StringBuilder(uri.length());
				builder.append("https://");
				writeHostName(request, 443, builder);
				String contextPath = request.getContextPath();
				if ((contextPath != null) && (!contextPath.isEmpty()) && (!"/".equals(contextPath))) {
					if (contextPath.charAt(contextPath.length() - 1) == '/') {
						builder.append(contextPath, 0, contextPath.length() - 1);
					} else {
						builder.append(contextPath);
					}
				}
				if ((uri != null) && (uri.length() > 0)) {
					if (uri.charAt(0) == '/') {
						builder.append(uri);
					} else {
						builder.append('/').append(uri);
					}
				}
				uri = builder.toString();
			} else {
				StringBuilder builder = new StringBuilder(uri.length());
				String contextPath = request.getContextPath();
				if ((contextPath != null) && (!contextPath.isEmpty()) && (!"/".equals(contextPath))) {
					if (contextPath.charAt(contextPath.length() - 1) == '/') {
						builder.append(contextPath, 0, contextPath.length() - 1);
					} else {
						builder.append(contextPath);
					}
				}
				if ((uri != null) && (uri.length() > 0)) {
					if (uri.charAt(0) == '/') {
						builder.append(uri);
					} else {
						builder.append('/').append(uri);
					}
				}
				uri = builder.toString();
			}
		} catch (Throwable throwable) {
			this.resourceProvider.log(throwable);
		}
		return uri;
	}

	public String getViewURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException {
		String name = servletClass.getName();
		ServletRegistration registration = request.getServletContext().getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String uri = registration.getInitParameter("DIMENG_VIEW_URI");
		if (StringHelper.isEmpty(uri)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		return uri;
	}

	public String getURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException {
		String name = servletClass.getName();
		ServletRegistration registration = request.getServletContext().getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String uri = registration.getInitParameter("DIMENG_SERVLET_URI");
		if (StringHelper.isEmpty(uri)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		return uri;
	}

	public String getViewForwardURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException {
		String name = servletClass.getName();
		ServletRegistration registration = request.getServletContext().getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String uri = registration.getInitParameter("DIMENG_VIEW_FORWARD_URI");
		if (StringHelper.isEmpty(uri)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		return uri;
	}

	public String getForwardURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException {
		String name = servletClass.getName();
		ServletRegistration registration = request.getServletContext().getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String uri = registration.getInitParameter("DIMENG_SERVLET_FORWARD_URI");
		if (StringHelper.isEmpty(uri)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		return uri;
	}

	public boolean hasPrompts(HttpServletRequest request, HttpServletResponse response, PromptLevel level) {
		if ((request == null) || (level == null)) {
			return false;
		}
		Map<PromptLevel, String> map = null;

		Object object = request.getAttribute(PROMPT_UUID);
		if (object != null) {
			try {
				map = (Map) object;
			} catch (Throwable t) {
			}
		}
		if ((map == null) || (map.size() == 0)) {
			try {
				SessionManager sessionManager = (SessionManager) this.resourceProvider
						.getResource(SessionManager.class);

				Session session = sessionManager.getSession(request, response, false);

				return session.getAttribute(level.parameterKey()) != null;
			} catch (Throwable t) {
				this.resourceProvider.log(t);
				return false;
			}
		}
		return !StringHelper.isEmpty((String) map.get(level));
	}

	public String getPrompt(HttpServletRequest request, HttpServletResponse response, PromptLevel level) {
		if ((request == null) || (level == null)) {
			return null;
		}
		String message = null;
		Map<PromptLevel, String> map = null;

		Object object = request.getAttribute(PROMPT_UUID);
		if (object != null) {
			try {
				map = (Map) object;
			} catch (Throwable t) {
			}
		}
		if ((map == null) || (map.size() == 0)) {
			try {
				SessionManager sessionManager = (SessionManager) this.resourceProvider
						.getResource(SessionManager.class);

				Session session = sessionManager.getSession(request, response, false);

				message = session == null ? null : session.removeAttribute(level.parameterKey());
			} catch (Throwable t) {
				this.resourceProvider.log(t);
			}
		} else {
			message = (String) map.remove(level);
		}
		return message;
	}

	public void prompt(HttpServletRequest request, HttpServletResponse response, PromptLevel level, String message) {
		if ((request == null) || (level == null) || (StringHelper.isEmpty(message))) {
			return;
		}
		Object object = request.getAttribute(PROMPT_UUID);
		Map<PromptLevel, String> map = null;
		if (object != null) {
			try {
				map = (Map) object;
			} catch (Throwable t) {
			}
		}
		if (map == null) {
			map = new HashMap();
			request.setAttribute(PROMPT_UUID, map);
		}
		map.put(level, message);
	}

	public void clearPrompts(HttpServletRequest request, HttpServletResponse response, PromptLevel level) {
		if ((request == null) || (level == null)) {
			return;
		}
		Cookie cookie = new Cookie(level.parameterKey(), "");
		cookie.setMaxAge(0);
		cookie.setValue("");
		response.addCookie(cookie);
		Object object = request.getAttribute(PROMPT_UUID);
		if (object != null) {
			Map<PromptLevel, String> map = null;
			try {
				map = (Map) object;
				map.remove(level);
			} catch (Throwable t) {
			}
		}
	}

	public void clearAll(HttpServletRequest request, HttpServletResponse response) {
		if (request == null) {
			return;
		}
		for (PromptLevel level : PromptLevel.values()) {
			Cookie cookie = new Cookie(level.parameterKey(), "");
			cookie.setMaxAge(0);
			cookie.setValue("");
			response.addCookie(cookie);
		}
		Object object = request.getAttribute(PROMPT_UUID);
		if (object != null) {
			Map<PromptLevel, String> map = null;
			try {
				map = (Map) object;
				map.clear();
			} catch (Throwable t) {
			}
		}
	}

	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String location)
			throws ServletException, IOException {
		if ((request == null) || (response == null) || (StringHelper.isEmpty(location))) {
			return;
		}
		Object object = request.getAttribute(PROMPT_UUID);
		if (object == null) {
			response.sendRedirect(location);
			return;
		}
		Map<PromptLevel, String> map = null;
		try {
			map = (Map) object;
			SessionManager sessionManager = (SessionManager) this.resourceProvider.getResource(SessionManager.class);

			Session session = sessionManager.getSession(request, response, true);
			for (Map.Entry<PromptLevel, String> entry : map.entrySet()) {
				session.setAttribute(((PromptLevel) entry.getKey()).parameterKey(), (String) entry.getValue());
			}
			response.sendRedirect(location);
		} catch (Throwable t) {
			t.printStackTrace();
			response.sendRedirect(location);
		}
	}

	public void forward(HttpServletRequest request, HttpServletResponse response, String path)
			throws ServletException, IOException {
		String contextPath = request.getContextPath();
		if ((StringHelper.isEmpty(contextPath)) || ("/".equals(contextPath))) {
			request.getRequestDispatcher(path).forward(request, response);
		} else if (path.startsWith(contextPath)) {
			request.getRequestDispatcher(path.substring(contextPath.length())).forward(request, response);
		} else {
			request.getRequestDispatcher(path).forward(request, response);
		}
	}

	public Prompter getPrompter(HttpServletRequest request) {
		Object object = request.getAttribute(PROMPT_UUID);
		Map<PromptLevel, String> map = null;
		if (object != null) {
			try {
				map = (Map) object;
			} catch (Throwable t) {
			}
		}
		if (map == null) {
			map = new HashMap();
			request.setAttribute(PROMPT_UUID, map);
		}
		return new MapPrompter(map);
	}

	private class MapPrompter implements Prompter {
		Map<PromptLevel, String> map;

		public MapPrompter(Map<PromptLevel, String> map2) {
			this.map = map2;
		}

		public void prompt(PromptLevel level, String message) {
			if ((level == null) || (StringHelper.isEmpty(message))) {
				return;
			}
			this.map.put(level, message);
		}

		public void clearPrompts(PromptLevel level) {
			this.map.remove(level);
		}

		public void clearAllPrompts() {
			this.map.clear();
		}
	}

	public synchronized void initialize(final ServletContext servletContext, final Rewriter rewriter,
			Set<Class<? extends HttpServlet>> servlets) {
		if (this.initilized) {
			return;
		}
		String contextPath = servletContext.getContextPath();
		if ((StringHelper.isEmpty(contextPath)) || ("/".equals(contextPath))) {
			this.contextURI = "";
		} else {
			this.contextURI = contextPath;
		}
		if (rewriter == null) {
			return;
		}
		new Thread() {
			public void run() {
				DefaultController.this.load(servletContext, rewriter, DefaultController.this.contextURI,
						rewriter.getViewRoot());
			}
		}.start();
		try {
			SystemDefine systemDefine = this.resourceProvider.getSystemDefine();
			if (!systemDefine.isStrictMVC()) {
				if (this.resourceProvider.getResourceRetention() == ResourceRetention.PRODUCTION) {
					ServletRegistration.Dynamic dynamic = servletContext.addServlet(PrdViewController.class.getName(),
							PrdViewController.class);
					if (dynamic != null) {
						dynamic.addMapping(new String[] { '*' + rewriter.getViewSuffix() });
						dynamic.setLoadOnStartup(0);
					}
				} else {
					ServletRegistration.Dynamic dynamic = servletContext
							.addServlet(DefaultViewController.class.getName(), DefaultViewController.class);
					if (dynamic != null) {
						dynamic.addMapping(new String[] { '*' + rewriter.getViewSuffix() });
						dynamic.setLoadOnStartup(0);
					}
				}
			}
		} catch (ResourceNotFoundException exception) {
		}
		try {
			FileStore fileStore = (FileStore) this.resourceProvider.getResource(FileStore.class);
			ServletRegistration.Dynamic dynamic = servletContext.addServlet(FileStoreServlet.class.getName(),
					FileStoreServlet.class);
			if (dynamic != null) {
				dynamic.addMapping(new String[] { fileStore.getUploadURI() + "/*" });
				dynamic.setLoadOnStartup(0);
			}
		} catch (ResourceNotFoundException exception) {
		}
		if ((servlets != null) && (servlets.size() > 0)) {
			if ((StringHelper.isEmpty(contextPath)) || ("/".equals(contextPath))) {
				loadServlet(servletContext, rewriter, servlets);
			} else {
				loadServlet(servletContext, rewriter, servlets, contextPath);
			}
		}
	}

	private void loadServlet(ServletContext servletContext, Rewriter rewriter,
			Set<Class<? extends HttpServlet>> servlets) {
		Pattern itemPattern = Pattern.compile("^\\w+\\" + rewriter.getViewSuffix() + "$");
		for (Class<? extends HttpServlet> servlet : servlets) {
			try {
				ServletRegistration.Dynamic dynamic = servletContext.addServlet(servlet.getName(), servlet);
				if (dynamic != null) {
					String uri = rewriter.getURI(servletContext.getContextPath(), servlet);

					dynamic.addMapping(new String[] { uri });
					dynamic.setInitParameter("DIMENG_SERVLET_URI", uri);
					dynamic.setInitParameter("DIMENG_SERVLET_FORWARD_URI", uri);
					String viewURI = rewriter.getViewURI(servletContext.getContextPath(), servlet);

					dynamic.setInitParameter("DIMENG_VIEW_URI", viewURI);
					dynamic.setInitParameter("DIMENG_VIEW_FORWARD_URI", viewURI);

					dynamic.setInitParameter("DIMENG_VIEW_FILE_URI", rewriter.getViewFilePath(servlet));

					PagingServlet pagingServlet = (PagingServlet) servlet.getAnnotation(PagingServlet.class);
					if (pagingServlet != null) {
						String path = rewriter.getPathURI(null, servlet);
						PagingControllerServlet controllerServlet = new PagingControllerServlet(pagingServlet, servlet,
								path, itemPattern);

						dynamic.setInitParameter("DIMENG_PAGING_URI", path);
						ServletRegistration.Dynamic paging = servletContext.addServlet(path, controllerServlet);
						if (paging != null) {
							paging.addMapping(new String[] { path + "*" });
						}
					}
				}
			} catch (Throwable throwable) {
				this.resourceProvider.log(throwable);
			}
		}
	}

	private void loadServlet(ServletContext servletContext, Rewriter rewriter,
			Set<Class<? extends HttpServlet>> servlets, String contextPath) {
		Pattern itemPattern = Pattern.compile("^\\w+\\" + rewriter.getViewSuffix() + "$");
		for (Class<? extends HttpServlet> servlet : servlets) {
			try {
				ServletRegistration dynamic = servletContext.addServlet(servlet.getName(), servlet);
				if (dynamic == null) {
					dynamic = servletContext.getServletRegistration(servlet.getName());
					if (dynamic == null) {
					}
				} else {
					String uri = rewriter.getURI(servletContext.getContextPath(), servlet);

					String pattern = uri.substring(contextPath.length());
					dynamic.addMapping(new String[] { pattern });
					dynamic.setInitParameter("DIMENG_SERVLET_URI", uri);
					dynamic.setInitParameter("DIMENG_SERVLET_FORWARD_URI", pattern);
					String viewURI = rewriter.getViewURI(servletContext.getContextPath(), servlet);

					dynamic.setInitParameter("DIMENG_VIEW_URI", viewURI);
					dynamic.setInitParameter("DIMENG_VIEW_FORWARD_URI", viewURI.substring(contextPath.length()));

					dynamic.setInitParameter("DIMENG_VIEW_FILE_URI", rewriter.getViewFilePath(servlet));

					PagingServlet pagingServlet = (PagingServlet) servlet.getAnnotation(PagingServlet.class);
					if (pagingServlet != null) {
						String path = rewriter.getPathURI(contextPath, servlet);
						PagingControllerServlet controllerServlet = new PagingControllerServlet(pagingServlet, servlet,
								path, itemPattern);

						dynamic.setInitParameter("DIMENG_PAGING_URI", path);
						path = path.substring(contextPath.length());
						ServletRegistration.Dynamic paging = servletContext.addServlet(path, controllerServlet);
						if (paging != null) {
							paging.addMapping(new String[] { path + "*" });
						}
					}
				}
			} catch (Throwable throwable) {
				this.resourceProvider.log(throwable);
			}
		}
	}

	public String getPagingURI(HttpServletRequest request, Class<? extends Servlet> servletClass)
			throws ServletNotFoundException {
		String name = servletClass.getName();
		ServletRegistration registration = request.getServletContext().getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String uri = registration.getInitParameter("DIMENG_PAGING_URI");
		if (StringHelper.isEmpty(uri)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		return uri;
	}

	public void forwardController(HttpServletRequest request, HttpServletResponse response,
			Class<? extends HttpServlet> controller) throws ServletException, IOException {
		request.getRequestDispatcher(getForwardURI(request, controller)).forward(request, response);
	}

	public void forwardView(HttpServletRequest request, HttpServletResponse response,
			Class<? extends HttpServlet> controller) throws ServletException, IOException {
		long t = System.currentTimeMillis();
		ServletContext servletContext = request.getServletContext();
		String name = controller.getName();
		ServletRegistration registration = servletContext.getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String viewFile = registration.getInitParameter("DIMENG_VIEW_FILE_URI");
		if (StringHelper.isEmpty(viewFile)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		RequestDispatcher dispatcher = servletContext.getRequestDispatcher(viewFile);

		dispatcher.forward(request, response);
		if (this.resourceProvider.getResourceRetention() != ResourceRetention.PRODUCTION) {
			this.resourceProvider.log(String.format("视图文件: %s%s, 响应耗时: %s ms", new Object[] {
					this.resourceProvider.getContextPath(), viewFile, Long.toString(System.currentTimeMillis() - t) }));
		}
	}

	public String getPagingItemURI(HttpServletRequest request, Class<? extends Servlet> servletClass, long id)
			throws ServletNotFoundException {
		return getPagingItemURI(request, servletClass, Long.toString(id));
	}

	public String getPagingItemURI(HttpServletRequest request, Class<? extends Servlet> servletClass, int id)
			throws ServletNotFoundException {
		return getPagingItemURI(request, servletClass, Integer.toString(id));
	}

	public String getPagingItemURI(HttpServletRequest request, Class<? extends Servlet> servletClass, String id)
			throws ServletNotFoundException {
		String name = servletClass.getName();
		ServletRegistration registration = request.getServletContext().getServletRegistration(name);
		if (registration == null) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		String uri = registration.getInitParameter("DIMENG_PAGING_URI");
		if (StringHelper.isEmpty(uri)) {
			throw new ServletNotFoundException(String.format("找不到Servlet注册信息:%s", new Object[] { name }));
		}
		StringBuilder builder = new StringBuilder();
		builder.append(uri).append(id).append(this.resourceProvider.getSystemDefine().getRewriter().getViewSuffix());

		return builder.toString();
	}

	private void load(ServletContext servletContext, Rewriter rewriter, String contextPath, String root) {
		Set<String> pathes = servletContext.getResourcePaths(root);
		if ((pathes == null) || (pathes.size() == 0)) {
			return;
		}
		String viewRoot = rewriter.getViewRoot();
		String suffix = rewriter.getViewFileSuffix();
		String uriSuffix = rewriter.getViewSuffix();
		StringBuilder builder = new StringBuilder();
		for (String path : pathes) {
			if (path.charAt(path.length() - 1) == '/') {
				load(servletContext, rewriter, contextPath, path);
			} else if (path.endsWith(suffix)) {
				builder.append(contextPath).append(path, viewRoot.length(), path.length() - suffix.length());

				builder.append(uriSuffix);
				String key = builder.toString();
				this.cachedPathes.put(key, path);
				this.cachedRevertPathes.put(contextPath + path, key);
				builder.setLength(0);
			}
		}
	}

	public String getViewFile(HttpServletRequest request) throws ServletNotFoundException {
		if (request == null) {
			return null;
		}
		return (String) this.cachedPathes.get(request.getRequestURI());
	}

	public void redirectLogin(HttpServletRequest request, HttpServletResponse response, String loginURI)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		if (this.cachedRevertPathes.containsKey(uri)) {
			uri = (String) this.cachedRevertPathes.get(uri);
		}
		if (uri.equals(loginURI)) {
			sendRedirect(request, response, loginURI);
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(loginURI).append("?_z=").append(uri);
			String queryString = request.getQueryString();
			if (!StringHelper.isEmpty(queryString)) {
				builder.append("%3f").append(URLEncoder.encode(queryString, this.resourceProvider.getCharset()));
			}
			sendRedirect(request, response, builder.toString());
		}
	}

	public void initilize(Connection connection) throws Throwable {
	}
}
