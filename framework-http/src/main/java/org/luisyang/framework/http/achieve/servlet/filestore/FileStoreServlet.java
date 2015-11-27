package org.luisyang.framework.http.achieve.servlet.filestore;

import org.luisyang.framework.http.upload.FileInformation;
import org.luisyang.framework.http.upload.FileStore;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;
import org.luisyang.util.StringHelper;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileStoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(servletContext);

		FileStore fileStore = (FileStore) resourceProvider.getResource(FileStore.class);
		String viewURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		if ((!StringHelper.isEmpty(contextPath)) && (!"/".equals(contextPath))) {
			viewURI = viewURI.substring(contextPath.length());
		}
		String home = fileStore.getUploadURI();
		int startIndex = home.length() + 1;
		if (startIndex >= viewURI.length()) {
			response.sendError(404, viewURI);
			return;
		}
		viewURI = viewURI.substring(startIndex);
		String[] items = viewURI.split("/");
		if (items.length != 5) {
			response.sendError(404, viewURI);
			return;
		}
		try {
			final int type = Integer.parseInt(items[0]);
			final int year = Integer.parseInt(items[1]);
			final int month = Integer.parseInt(items[2]);
			final int day = Integer.parseInt(items[3]);
			int idx = items[4].lastIndexOf('.');
			final int id;
			final String suffix;
			if (idx == -1) {
				id = Integer.parseInt(items[4]);
				suffix = null;
			} else {
				id = Integer.parseInt(items[4].substring(0, idx));
				if (idx == items[4].length() - 1) {
					suffix = null;
				} else {
					suffix = items[4].substring(idx + 1);
				}
			}
			if (StringHelper.isEmpty(suffix)) {
				response.setContentType("application/octet-stream");
			} else {
				String contentType = servletContext.getMimeType(items[4].toLowerCase());
				if (contentType == null) {
					response.setContentType("application/octet-stream");
				} else {
					response.setContentType(contentType);
				}
			}
			fileStore.read(new FileInformation() {
				public int getYear() {
					return year;
				}

				public int getType() {
					return type;
				}

				public String getSuffix() {
					return suffix;
				}

				public int getMonth() {
					return month;
				}

				public int getId() {
					return id;
				}

				public int getDay() {
					return day;
				}
			}, response.getOutputStream());
		} catch (Throwable t) {
			response.sendError(404, viewURI);
			return;
		}
	}
}
