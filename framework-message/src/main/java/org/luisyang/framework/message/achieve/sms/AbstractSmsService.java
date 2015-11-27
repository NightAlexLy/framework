package org.luisyang.framework.message.achieve.sms;

import java.sql.Connection;
import java.sql.SQLException;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.message.MessageProvider;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.service.AbstractService;
import org.luisyang.framework.service.ServiceResource;

public abstract class AbstractSmsService extends AbstractService {
	public AbstractSmsService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected SQLConnectionProvider getConnectionProvider() {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return (SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(MessageProvider.class));
	}

	protected Connection getConnection() throws ResourceNotFoundException, SQLException {
		SystemDefine systemDefine = this.serviceResource.getSystemDefine();
		return ((SQLConnectionProvider) this.serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(MessageProvider.class)))
						.getConnection(systemDefine.getSchemaName(MessageProvider.class));
	}
}
