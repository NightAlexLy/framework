package org.luisyang.framework.message.email.entity;

/**
 * 邮件任务
 * 
 * @author LuisYang
 */
public class EmailTask {
	/**
	 * 唯一标识
	 */
	public long id;
	/**
	 * 类型
	 */
	public int type;
	/**
	 * 标题
	 */
	public String subject;
	/**
	 * 内容
	 */
	public String content;
	/**
	 * 地址
	 */
	public String[] addresses;
}
