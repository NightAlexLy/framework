package org.luisyang.framework.message.sms;

import org.luisyang.framework.message.sms.entity.SmsTask;
import org.luisyang.framework.service.Service;

/**
 * 提取
 */
public abstract interface Extracter extends Service {

	/**
	 * 提取
	 * @param maxCount
	 * @param expiresMinutes
	 * @return
	 * @throws Throwable
	 */
	public abstract SmsTask[] extract(int maxCount, int expiresMinutes) throws Throwable;

	/**
	 * 已发送短信标记
	 * @param id
	 * @param success
	 * @param extra
	 * @throws Throwable
	 */
	public abstract void mark(long id, boolean success, String extra) throws Throwable;
}
