package org.luisyang.framework.http.achieve.servlet.paging;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.servlet.Rewriter;
import org.luisyang.framework.http.servlet.annotation.PagingServlet;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;
import org.luisyang.util.parser.IntegerParser;

public class PagingControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Pattern PAGING_PATTERN = Pattern.compile("^\\d+/?$");
	protected final Pattern itemPattern;
	protected final PagingServlet pagingServlet;
	protected final Class<? extends HttpServlet> paging;
	protected final String path;

	public PagingControllerServlet(PagingServlet pagingServlet, Class<? extends HttpServlet> paging, String path,
			Pattern itemPattern) {
		this.pagingServlet = pagingServlet;
		this.paging = paging;
		this.path = path;
		this.itemPattern = itemPattern;
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(getServletContext());

		Controller controller = (Controller) resourceProvider.getResource(Controller.class);
		Rewriter rewriter = resourceProvider.getSystemDefine().getRewriter();
		if (uri.length() <= this.path.length()) {
			response.sendRedirect(controller.getViewURI(request, this.paging));
			return;
		}
		uri = uri.substring(this.path.length());
		Class<? extends HttpServlet> itemServlet = this.pagingServlet.itemServlet();
		if (!Modifier.isAbstract(itemServlet.getModifiers())) {
			Matcher matcher = this.itemPattern.matcher(uri);
			if (matcher.matches()) {
				String id = uri.substring(0, uri.length() - rewriter.getViewSuffix().length());

				StringBuilder to = new StringBuilder();
				if (this.pagingServlet.viewItem()) {
					to.append(controller.getViewForwardURI(request, this.pagingServlet.itemServlet()));
				} else {
					to.append(controller.getForwardURI(request, this.pagingServlet.itemServlet()));
				}
				to.append('?').append(this.pagingServlet.itemToken()).append('=').append(id);

				request.getRequestDispatcher(to.toString()).forward(request, response);

				return;
			}
		}
		Matcher matcher = PAGING_PATTERN.matcher(uri);
		if (matcher.matches()) {
			int currentPage = 0;
			int endIndex = uri.length() - 1;
			if (uri.charAt(endIndex) == '/') {
				currentPage = IntegerParser.parse(uri.substring(0, endIndex));
			} else {
				currentPage = IntegerParser.parse(uri);
			}
			StringBuilder to = new StringBuilder();
			if (this.pagingServlet.viewPaging()) {
				to.append(controller.getViewForwardURI(request, this.paging));
			} else {
				to.append(controller.getForwardURI(request, this.paging));
			}
			to.append('?').append(this.pagingServlet.currentToken()).append('=').append(currentPage);

			request.getRequestDispatcher(to.toString()).forward(request, response);

			return;
		}
		response.sendError(404);
	}
}