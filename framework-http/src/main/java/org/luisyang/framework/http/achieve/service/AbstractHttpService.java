package org.luisyang.framework.http.achieve.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.http.session.SessionManager;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.service.AbstractService;
import org.luisyang.framework.service.ServiceResource;

public abstract class AbstractHttpService extends AbstractService {
	public AbstractHttpService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected SQLConnectionProvider getConnectionProvider() {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return (SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(SessionManager.class));
	}

	protected Connection getConnection() throws ResourceNotFoundException, SQLException {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return ((SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(SessionManager.class)))
						.getConnection(systemDefine.getSchemaName(SessionManager.class));
	}

	protected Connection getConnection(String schemaName) throws ResourceNotFoundException, SQLException {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return ((SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(SessionManager.class))).getConnection(schemaName);
	}
}
