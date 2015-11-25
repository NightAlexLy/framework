package org.luisyang.framework.resource;

/**
 * 提示接口
 * 
 * @author LuisYang
 */
public abstract interface Prompter {

	/**
	 * 提示
	 * @param level 级别
	 * @param message 消息
	 */
	public abstract void prompt(PromptLevel level, String message);

	/**
	 * 清空指定级别的提示
	 * @param level 级别
	 */
	public abstract void clearPrompts(PromptLevel level);

	/**
	 * 清空所有级别的消息
	 */
	public abstract void clearAllPrompts();
}
