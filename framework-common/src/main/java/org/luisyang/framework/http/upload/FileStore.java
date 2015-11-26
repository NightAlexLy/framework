package org.luisyang.framework.http.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.config.Envionment;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.util.StringHelper;

/**
 * 文件流
 * 
 * @author LuisYang
 */
public abstract class FileStore extends Resource implements Envionment {
	
	private File home;
	private String uploadURI = null;

	public FileStore(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 唯一标识类
	 */
	public final Class<? extends Resource> getIdentifiedType() {
		return FileStore.class;
	}

	/**
	 * 生成新的文件
	 * @param type 类型 
	 * @param suffix 后缀
	 * @return
	 * @throws Throwable
	 */
	public abstract String newCode(int type, String suffix) throws Throwable;

	/**
	 * 生成基本信息模型
	 * @param fileCode
	 * @return
	 */
	public abstract FileInformation getFileInformation(String fileCode);

	/**
	 * 获得标识获得地址
	 */
	public String get(String key) {
		if (StringHelper.isEmpty(key)) {
			return null;
		}
		FileInformation fileInformation = getFileInformation(key);
		if (fileInformation == null) {
			return ((ConfigureProvider) this.resourceProvider.getResource(ConfigureProvider.class)).getProperty(key);
		}
		return getURL(fileInformation);
	}

	public void set(String key, String value) {
	}

	/**
	 * 获得文件地址
	 * @param fileCode
	 * @return
	 */
	public String getURL(String fileCode) {
		return getURL(getFileInformation(fileCode));
	}

	/**
	 * 通过基本信息获得地址
	 * @param fileInformation
	 * @return
	 */
	public abstract String getURL(FileInformation fileInformation);

	/**
	 * 上传
	 * @param type
	 * @param uploadFiles
	 * @return
	 * @throws Throwable
	 */
	public abstract String[] upload(int type, UploadFile... uploadFiles) throws Throwable;

	/**
	 * 写入
	 * @param fileInformation
	 * @param inputStream
	 * @throws IOException
	 */
	public abstract void write(FileInformation fileInformation, InputStream inputStream) throws IOException;

	/**
	 * 读取
	 * @param fileInformation
	 * @param outputStream
	 * @throws IOException
	 */
	public abstract void read(FileInformation fileInformation, OutputStream outputStream) throws IOException;

	/**
	 * 对内容进行编码
	 * @param value
	 * @return
	 */
	public abstract String encode(String value);

	/**
	 * 获得文件
	 * @return
	 */
	public synchronized File getHome() {
		if (this.home == null) {
			String stringHome = this.resourceProvider.getInitParameter("fileStore.home");
			if (StringHelper.isEmpty(stringHome)) {
				this.home = new File(System.getProperty("user.home"), "fileStore");
			} else {
				File file = new File(stringHome);
				if (file.isAbsolute()) {
					this.home = file;
				} else {
					String contextHome = this.resourceProvider.getHome();
					this.home = new File(contextHome, stringHome);
				}
			}
			this.home.mkdirs();
		}
		return this.home;
	}

	/**
	 * 获得上传地址
	 * @return
	 */
	public synchronized String getUploadURI() {
		if (this.uploadURI == null) {
			String value = this.resourceProvider.getInitParameter("fileStore.uploadURI");
			if (StringHelper.isEmpty(value)) {
				this.uploadURI = "/fileStore";
			} else if (value.charAt(0) != '/') {
				this.uploadURI = ('/' + value);
			} else {
				this.uploadURI = value;
			}
		}
		return this.uploadURI;
	}
}