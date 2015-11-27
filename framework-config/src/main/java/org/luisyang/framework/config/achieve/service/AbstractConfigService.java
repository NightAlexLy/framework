package org.luisyang.framework.config.achieve.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.service.AbstractService;
import org.luisyang.framework.service.ServiceResource;

/**
 * 配置服务
 * 
 *  @author Luisyang
 */
public abstract class AbstractConfigService extends AbstractService {
	public AbstractConfigService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/**
	 * 获得SQL数据源供应商
	 * @return
	 */
	protected SQLConnectionProvider getConnectionProvider() {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return (SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(ConfigureProvider.class));
	}

	/**
	 * 获得连接
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws SQLException
	 */
	protected Connection getConnection() throws ResourceNotFoundException, SQLException {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return ((SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(ConfigureProvider.class)))
						.getConnection(systemDefine.getSchemaName(ConfigureProvider.class));
	}
}
