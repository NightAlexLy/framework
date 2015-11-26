package org.luisyang.framework.http.upload;

/**
 * 文件基本信息模型
 * 
 * @author LuisYang
 */
public abstract class FileInformation {
	
	/**
	 * 唯一标识符
	 * @return
	 */
	public abstract int getId();

	/**
	 * 年
	 * @return
	 */
	public abstract int getYear();

	/**
	 * 月
	 * @return
	 */
	public abstract int getMonth();

	/**
	 * 日
	 * @return
	 */
	public abstract int getDay();

	/**
	 * 类型
	 * @return
	 */
	public abstract int getType();

	/**
	 * 获得后缀
	 * @return
	 */
	public abstract String getSuffix();
}
