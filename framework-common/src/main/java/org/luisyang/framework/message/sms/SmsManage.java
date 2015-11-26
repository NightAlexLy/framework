package org.luisyang.framework.message.sms;

import org.luisyang.framework.message.sms.entity.SmsTask;
import org.luisyang.framework.service.Service;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;

/**
 * 短信管理
 * 
 *  @author LuisYang
 */
public abstract interface SmsManage extends Service {
	
	/**
	 * 搜索未发送的短信任务
	 * @param paramSendTaskQuery
	 * @param paramPaging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<SmsTask> searchUnsendTask(SendTaskQuery paramSendTaskQuery, Paging paramPaging)
			throws Throwable;

	/**
	 * 搜索已发送的短信任务
	 * @param paramSendTaskQuery
	 * @param paramPaging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<SmsTask> searchSendTask(SendTaskQuery paramSendTaskQuery, Paging paramPaging)
			throws Throwable;

	public static abstract interface SendTaskQuery {
		/**
		 * 唯一标识
		 * @return
		 */
		public abstract int getId();

		/**
		 * 类型
		 * @return
		 */
		public abstract int getType();

		/**
		 * 消息
		 * @return
		 */
		public abstract String getMessage();

		/**
		 * 状态
		 * @return
		 */
		public abstract String getStatus();
	}
}