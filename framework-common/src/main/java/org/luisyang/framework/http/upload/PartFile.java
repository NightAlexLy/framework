package org.luisyang.framework.http.upload;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

/**
 *  <b>部分下载</b>
 *  <p>Servlet 3.0新特性——文件上传接口</p>
 *  <p>http://pisces-java.iteye.com/blog/723125</p>
 */
public class PartFile implements UploadFile {
	
	protected final Part part;
	protected String suffix;

	public PartFile(Part part) {
		this.part = part;
	}

	/**
	 * 获得后缀
	 */
	public String getSuffix() {
		if (this.suffix == null) {
			String header = this.part.getHeader("content-disposition");
			int idx = header.lastIndexOf(".");
			if (idx == -1) {
				this.suffix = "";
			} else {
				this.suffix = header.substring(header.lastIndexOf(".") + 1, header.length() - 1);
			}
		}
		return this.suffix;
	}

	/**
	 * 获得文件输入流
	 */
	public InputStream getInputStream() throws IOException {
		return this.part.getInputStream();
	}
}