package org.luisyang.framework.http.achieve.servlet.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.servlet.Rewriter;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;
import org.luisyang.util.StringHelper;
import org.luisyang.util.collection.LRUMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DefaultViewController extends HttpServlet {
	protected final Map<String, String> dynamicPathes = new LRUMap(100000);
	protected String[] welcomeFileList = null;
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		cache(config.getServletContext());
	}

	private void cache(ServletContext servletContext) {
		ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(servletContext);
		try {
			Rewriter rewriter = resourceProvider.getSystemDefine().getRewriter();

			String viewRoot = rewriter.getViewRoot();
			final String suffix = rewriter.getViewFileSuffix();
			final String uriSuffix = rewriter.getViewSuffix();
			if ((StringHelper.isEmpty(viewRoot)) || (StringHelper.isEmpty(suffix))
					|| (StringHelper.isEmpty(uriSuffix))) {
				return;
			}
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				final ArrayList<String> list = new ArrayList(5);
				parser.parse(servletContext.getResourceAsStream("/WEB-INF/web.xml"), new DefaultHandler() {
					String file = null;
					boolean matched = false;

					public void startElement(String uri, String localName, String qName, Attributes attributes)
							throws SAXException {
						if ("welcome-file".equalsIgnoreCase(qName)) {
							this.matched = true;
						}
					}

					public void endElement(String uri, String localName, String qName) throws SAXException {
						if (("welcome-file".equalsIgnoreCase(qName)) && (this.file.endsWith(uriSuffix))) {
							list.add(this.file.substring(0, this.file.length() - uriSuffix.length()) + suffix);
						}
						this.matched = false;
					}

					public void characters(char[] ch, int start, int length) throws SAXException {
						if (this.matched) {
							this.file = new String(ch, start, length);
						}
					}
				});
				this.welcomeFileList = ((list == null) || (list.size() == 0) ? null
						: (String[]) list.toArray(new String[list.size()]));
			} catch (Throwable t) {
			}
		} catch (Throwable throwable) {
			resourceProvider.log(throwable);
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(servletContext);

		Controller controller = (Controller) resourceProvider.getResource(Controller.class);
		String path = controller.getViewFile(request);
		long t = System.currentTimeMillis();
		if (path == null) {
			String viewURI = request.getRequestURI();
			path = (String) this.dynamicPathes.get(viewURI);
			if (path == null) {
				Rewriter rewriter = resourceProvider.getSystemDefine().getRewriter();

				String viewSuffix = rewriter.getViewSuffix();
				String contextPath = request.getContextPath();
				if (viewURI.endsWith(viewSuffix)) {
					StringBuilder builder = new StringBuilder(rewriter.getViewRoot());
					if ((StringHelper.isEmpty(contextPath)) || ("/".equals(contextPath))) {
						builder.append(viewURI, 0, viewURI.length() - viewSuffix.length());
					} else {
						builder.append(viewURI, contextPath.length(), viewURI.length() - viewSuffix.length());
					}
					builder.append(rewriter.getViewFileSuffix());
					path = builder.toString();
				} else {
					if ((this.welcomeFileList == null) || (this.welcomeFileList.length == 0)) {
						response.sendError(404, viewURI);

						return;
					}
					StringBuilder builder = new StringBuilder(rewriter.getViewRoot());
					if ((StringHelper.isEmpty(contextPath)) || ("/".equals(contextPath))) {
						builder.append(viewURI);
					} else {
						builder.append(viewURI, contextPath.length(), viewURI.length());
					}
					if (viewURI.charAt(viewURI.length() - 1) != '/') {
						builder.append('/');
					}
					int length = builder.length();
					for (String welcomeFile : this.welcomeFileList) {
						builder.append(welcomeFile);
						String tmpPath = builder.toString();
						builder.setLength(length);
						if (servletContext.getResource(tmpPath) != null) {
							path = tmpPath;
							break;
						}
					}
					if (path == null) {
						response.sendError(404, viewURI);

						return;
					}
				}
				this.dynamicPathes.put(viewURI, path);
			}
		}
		RequestDispatcher dispatcher = servletContext.getRequestDispatcher(path);

		dispatcher.forward(request, response);
		resourceProvider.log(String.format("视图文件: %s%s, 响应耗时: %s ms", new Object[] { resourceProvider.getContextPath(),
				path, Long.toString(System.currentTimeMillis() - t) }));
	}
}
