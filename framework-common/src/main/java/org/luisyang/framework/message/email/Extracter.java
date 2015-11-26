package org.luisyang.framework.message.email;

import org.luisyang.framework.message.email.entity.EmailTask;
import org.luisyang.framework.service.Service;

/**
 * 提取 
 */
public abstract interface Extracter extends Service {
	/**
	 * 提取邮件
	 * @param paramInt1
	 * @param paramInt2
	 * @return
	 * @throws Throwable
	 */
	public abstract EmailTask[] extract(int maxCount, int expiresMinutes) throws Throwable;

	/**
	 * 已发送标记
	 * @param id
	 * @throws Throwable
	 */
	public abstract void mark(long id) throws Throwable;
}
