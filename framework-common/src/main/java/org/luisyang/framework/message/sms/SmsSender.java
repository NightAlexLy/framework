package org.luisyang.framework.message.sms;

import org.luisyang.framework.service.Service;

/**
 * 短信发送
 * 
 * @author LuisYang
 */
public abstract interface SmsSender extends Service {
	
	/**
	 * 短信发送
	 * @param id
	 * @param content
	 * @param receivers
	 * @throws Throwable
	 */
	public abstract void send(int id, String content, String... receivers) throws Throwable;
}
