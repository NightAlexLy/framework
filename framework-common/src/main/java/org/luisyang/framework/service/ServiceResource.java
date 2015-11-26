package org.luisyang.framework.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceInvalidatedException;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.service.exception.ServiceNotFoundException;

public abstract interface ServiceResource extends ServiceSession, ResourceProvider {
	
	public abstract <R extends Resource> R getResource(Class<R> paramClass) throws ResourceNotFoundException;

	/**
	 * @deprecated
	 */
	public abstract <S extends Service> S getService(Class<S> paramClass)
			throws ServiceNotFoundException, ResourceInvalidatedException;

	/**
	 * 设置参数
	 * @param paramPreparedStatement
	 * @param paramCollection
	 * @throws SQLException
	 */
	public abstract void setParameters(PreparedStatement paramPreparedStatement, Collection<Object> paramCollection)
			throws SQLException;

	/**
	 * 设置参数
	 * @param paramPreparedStatement
	 * @param paramVarArgs
	 * @throws SQLException
	 */
	public abstract void setParameters(PreparedStatement paramPreparedStatement, Object... paramVarArgs)
			throws SQLException;
}
