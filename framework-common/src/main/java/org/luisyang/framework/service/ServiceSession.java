package org.luisyang.framework.service;

import java.sql.Connection;

import javax.management.ServiceNotFoundException;

import org.luisyang.framework.resource.Prompter;
import org.luisyang.framework.resource.ResourceNotFoundException;

import com.google.code.yanf4j.core.Controller;
import com.google.code.yanf4j.core.Session;

public abstract interface ServiceSession extends AutoCloseable, Prompter {
	
	public abstract Session getSession();

	public abstract <S extends Service> S getService(Class<S> paramClass)
			throws ServiceNotFoundException, ResourceInvalidatedException;

	public abstract void openTransactions(Connection paramConnection) throws Throwable;

	public abstract void openTransactions(int paramInt) throws Throwable;

	public abstract void commit(Connection paramConnection) throws Throwable;

	public abstract void rollback(Connection paramConnection) throws Throwable;

	public abstract boolean isTransactions() throws Throwable;

	public abstract void close();

	public abstract Controller getController() throws ResourceNotFoundException;

}
