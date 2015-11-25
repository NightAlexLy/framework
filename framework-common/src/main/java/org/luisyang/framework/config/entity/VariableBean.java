package org.luisyang.framework.config.entity;

/**
 * 变量模型接口
 */
public abstract interface VariableBean {

	/**
	 * 变量类型
	 * @return
	 */
	public abstract String getType();
	  
	/**
	 * 变量的唯一主键
	 * @return
	 */
    public abstract String getKey();
  
    /**
     * 变量值
     * @return
     */
    public abstract String getValue();
  
    /**
     * 变量描述
     * @return
     */
    public abstract String getDescription();
  
    /**
     * 是否初始化
     * @return
     */
    public abstract boolean isInit();
}
