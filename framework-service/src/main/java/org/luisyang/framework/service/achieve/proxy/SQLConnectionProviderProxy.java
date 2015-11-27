package org.luisyang.framework.service.achieve.proxy;

import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

public abstract class SQLConnectionProviderProxy extends SQLConnectionProvider {
	protected final SQLConnectionProvider connectionProvider;

	public SQLConnectionProviderProxy(InitParameterProvider parameterProvider, Logger logger,
			SQLConnectionProvider connectionProvider) {
		super(parameterProvider, logger);
		this.connectionProvider = connectionProvider;
	}

	public String getName() {
		return this.connectionProvider.getName();
	}

	public void close() {
	}

	public String prefixMatch(String value) {
		return this.connectionProvider.prefixMatch(value);
	}

	public String subfixMatch(String value) {
		return this.connectionProvider.subfixMatch(value);
	}

	public String allMatch(String value) {
		return this.connectionProvider.allMatch(value);
	}

	public int hashCode() {
		return this.connectionProvider.hashCode();
	}

	public boolean equals(Object obj) {
		return this.connectionProvider.equals(obj);
	}

	public String toString() {
		return this.connectionProvider.toString();
	}
}
