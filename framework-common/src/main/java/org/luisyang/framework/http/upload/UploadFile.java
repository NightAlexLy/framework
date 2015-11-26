package org.luisyang.framework.http.upload;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件
 * 
 * @author LuisYang
 */
public abstract interface UploadFile {
	
	/**
	 * 后缀
	 * @return
	 */
	public abstract String getSuffix();

	/**
	 * 输入流
	 * @return
	 * @throws IOException
	 */
	public abstract InputStream getInputStream() throws IOException;
}
