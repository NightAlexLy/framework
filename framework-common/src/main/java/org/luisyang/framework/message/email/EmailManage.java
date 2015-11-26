package org.luisyang.framework.message.email;

import org.luisyang.framework.message.email.entity.EmailTask;
import org.luisyang.framework.service.Service;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;

/**
 * 邮件管理
 *  
 */
public abstract interface EmailManage extends Service {

	/**
	 * 查询未发送邮件任务
	 * @param paramEmailTaskQuery
	 * @param paramPaging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<EmailTask> searchUnsendTask(EmailTaskQuery paramEmailTaskQuery, Paging paramPaging)
			throws Throwable;

	/**
	 * 查询已发送邮件任务
	 * @param paramEmailTaskQuery
	 * @param paramPaging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<EmailTask> searchSendTask(EmailTaskQuery paramEmailTaskQuery, Paging paramPaging)
			throws Throwable;

	/**
	 * 邮件查询模型
	 */
	public static abstract interface EmailTaskQuery {
		public abstract int getId();

		public abstract int getType();

		public abstract String getSubject();

		public abstract String getContent();

		public abstract String getStatus();
	}
}
