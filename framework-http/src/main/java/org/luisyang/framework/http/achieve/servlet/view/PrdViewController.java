package org.luisyang.framework.http.achieve.servlet.view;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.servlet.Rewriter;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;
import org.luisyang.util.StringHelper;

public class PrdViewController extends DefaultViewController {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(servletContext);

		Controller controller = (Controller) resourceProvider.getResource(Controller.class);
		String path = controller.getViewFile(request);
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
	}
}
