package org.luisyang.framework.message.email;

import org.luisyang.framework.service.Service;

/**
 * 邮件发送接口
 * 
 */
public abstract interface EmailSender extends Service {
	
	/**
	 * 发送
	 * @param type 类型
	 * @param subject 标题
	 * @param content 内容
	 * @param addresses 地址
	 * @throws Throwable
	 */
	public abstract void send(int type, String subject, String content, String... addresses)
			throws Throwable;
}
