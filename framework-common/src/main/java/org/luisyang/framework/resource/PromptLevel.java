package org.luisyang.framework.resource;

/**
 * 提示级别
 * 
 * @author LuisYang
 */
public enum PromptLevel {
	
	INFO, WARRING, ERROR;

	private PromptLevel() {
	}

	public String parameterKey() {
		return this.name();
	}
}
