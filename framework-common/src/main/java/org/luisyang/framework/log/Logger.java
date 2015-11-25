package org.luisyang.framework.log;

/**
 * 日志接口
 * 
 * @author LuisYang
 */
public abstract interface Logger {

	/**
	 * log记录抛出异常
	 * 
	 * @param throwable
	 */
	public abstract void log(Throwable throwable);

	/**
	 * log记录日志
	 * 
	 * @param throwable
	 */
	public abstract void log(String log);

	/**
	 * 错误日志记录
	 * 
	 * @param error
	 *            错误
	 * @param throwable
	 *            异常
	 */
	public abstract void error(String error, Throwable throwable);

	/**
	 * 错误日志记录
	 * 
	 * @param throwable
	 *            异常
	 */
	public abstract void error(Throwable throwable);

	/**
	 * info记录日志
	 * 
	 * @param throwable
	 *            异常
	 */
	public abstract void info(String info);
}
