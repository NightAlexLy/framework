package org.luisyang.framework.resource;

import java.sql.Connection;

/**
 * 资源抽象类
 */
public abstract class Resource implements AutoCloseable {

	protected final ResourceProvider resourceProvider;

	/**
	 * 初始化资源供应商
	 * 
	 * @param resourceProvider
	 */
	public Resource(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	/**
	 * 获得实现版本号
	 * 
	 * @return
	 */
	public int getAchieveVersion() {
		AchieveVersion achieveVersion = (AchieveVersion) getClass().getAnnotation(AchieveVersion.class);

		return achieveVersion == null ? 0 : achieveVersion.version();
	}

	/**
	 * 返回文件类型唯一标示
	 * @return
	 */
	public abstract Class<? extends Resource> getIdentifiedType();

	/**
	 * 初始化链接
	 * 
	 * @param connection
	 * @throws Throwable
	 */
	public abstract void initilize(Connection connection) throws Throwable;

	/**
	 * 关闭
	 */
	public void close() throws Exception {
	}

}
