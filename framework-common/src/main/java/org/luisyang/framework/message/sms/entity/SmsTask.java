package org.luisyang.framework.message.sms.entity;

import java.io.Serializable;

/**
 * 短信任务
 * 
 * @author LuisYang
 */
public class SmsTask implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 唯一标识
	 */
	public long id;
	/**
	 * 类型
	 */
	public int type;
	/**
	 * 内容
	 */
	public String content;
	/**
	 * 接受人
	 */
	public String[] receivers;
}
