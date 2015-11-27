package org.luisyang.framework.service.achieve.proxy;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.luisyang.framework.data.sql.SQLConnection;
import org.luisyang.util.StringHelper;

public class SQLConnectionProxy implements SQLConnection {
	SQLConnection connection;
	protected String previousSchema = null;

	public SQLConnectionProxy(SQLConnection connection) {
		this.connection = connection;
	}

	public void _release() throws SQLException {
		if (this.connection != null) {
			this.connection.close();
			this.connection = null;
		}
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.connection.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.connection.isWrapperFor(iface);
	}

	public Statement createStatement() throws SQLException {
		return new StatementProxy(this.connection.createStatement());
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new PreparedStatementProxy(this.connection.prepareStatement(sql));
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return this.connection.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return this.connection.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.connection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return this.connection.getAutoCommit();
	}

	public void commit() throws SQLException {
		this.connection.commit();
	}

	public void rollback() throws SQLException {
		this.connection.rollback();
	}

	public void close() throws SQLException {
		if (!StringHelper.isEmpty(this.previousSchema)) {
			this.connection.setSchema(this.previousSchema);
		}
	}

	public boolean isClosed() throws SQLException {
		return this.connection.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return this.connection.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		this.connection.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return this.connection.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		this.connection.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return this.connection.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		this.connection.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return this.connection.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return this.connection.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		this.connection.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return this.connection.createStatement(resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return this.connection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return this.connection.getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		this.connection.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		this.connection.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return this.connection.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.connection.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.connection.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return this.connection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return this.connection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return this.connection.prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException {
		return this.connection.createClob();
	}

	public Blob createBlob() throws SQLException {
		return this.connection.createBlob();
	}

	public NClob createNClob() throws SQLException {
		return this.connection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return this.connection.createSQLXML();
	}

	public boolean isValid(int timeout) throws SQLException {
		return this.connection.isValid(timeout);
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		this.connection.setClientInfo(name, value);
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		this.connection.setClientInfo(properties);
	}

	public String getClientInfo(String name) throws SQLException {
		return this.connection.getClientInfo(name);
	}

	public Properties getClientInfo() throws SQLException {
		return this.connection.getClientInfo();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return this.connection.createArrayOf(typeName, elements);
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return this.connection.createStruct(typeName, attributes);
	}

	public void setSchema(String schema) throws SQLException {
		if (StringHelper.isEmpty(schema)) {
			return;
		}
		if (this.previousSchema == null) {
			this.previousSchema = this.connection.getSchema();
		}
		this.connection.setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return this.connection.getSchema();
	}

	public void abort(Executor executor) throws SQLException {
		this.connection.abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		this.connection.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return this.connection.getNetworkTimeout();
	}

	class StatementProxy implements Statement {
		Statement statement;

		public StatementProxy(Statement statement) {
			this.statement = statement;
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return this.statement.unwrap(iface);
		}

		public ResultSet executeQuery(String sql) throws SQLException {
			return this.statement.executeQuery(sql);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return this.statement.isWrapperFor(iface);
		}

		public int executeUpdate(String sql) throws SQLException {
			return this.statement.executeUpdate(sql);
		}

		public void close() throws SQLException {
			this.statement.close();
		}

		public int getMaxFieldSize() throws SQLException {
			return this.statement.getMaxFieldSize();
		}

		public void setMaxFieldSize(int max) throws SQLException {
			this.statement.setMaxFieldSize(max);
		}

		public int getMaxRows() throws SQLException {
			return this.statement.getMaxRows();
		}

		public void setMaxRows(int max) throws SQLException {
			this.statement.setMaxRows(max);
		}

		public void setEscapeProcessing(boolean enable) throws SQLException {
			this.statement.setEscapeProcessing(enable);
		}

		public int getQueryTimeout() throws SQLException {
			return this.statement.getQueryTimeout();
		}

		public void setQueryTimeout(int seconds) throws SQLException {
			this.statement.setQueryTimeout(seconds);
		}

		public void cancel() throws SQLException {
			this.statement.cancel();
		}

		public SQLWarning getWarnings() throws SQLException {
			return this.statement.getWarnings();
		}

		public void clearWarnings() throws SQLException {
			this.statement.clearWarnings();
		}

		public void setCursorName(String name) throws SQLException {
			this.statement.setCursorName(name);
		}

		public boolean execute(String sql) throws SQLException {
			return this.statement.execute(sql);
		}

		public ResultSet getResultSet() throws SQLException {
			return this.statement.getResultSet();
		}

		public int getUpdateCount() throws SQLException {
			return this.statement.getUpdateCount();
		}

		public boolean getMoreResults() throws SQLException {
			return this.statement.getMoreResults();
		}

		public void setFetchDirection(int direction) throws SQLException {
			this.statement.setFetchDirection(direction);
		}

		public int getFetchDirection() throws SQLException {
			return this.statement.getFetchDirection();
		}

		public void setFetchSize(int rows) throws SQLException {
			this.statement.setFetchSize(rows);
		}

		public int getFetchSize() throws SQLException {
			return this.statement.getFetchSize();
		}

		public int getResultSetConcurrency() throws SQLException {
			return this.statement.getResultSetConcurrency();
		}

		public int getResultSetType() throws SQLException {
			return this.statement.getResultSetType();
		}

		public void addBatch(String sql) throws SQLException {
			this.statement.addBatch(sql);
		}

		public void clearBatch() throws SQLException {
			this.statement.clearBatch();
		}

		public int[] executeBatch() throws SQLException {
			return this.statement.executeBatch();
		}

		public Connection getConnection() throws SQLException {
			return SQLConnectionProxy.this;
		}

		public boolean getMoreResults(int current) throws SQLException {
			return this.statement.getMoreResults(current);
		}

		public ResultSet getGeneratedKeys() throws SQLException {
			return this.statement.getGeneratedKeys();
		}

		public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
			return this.statement.executeUpdate(sql, autoGeneratedKeys);
		}

		public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
			return this.statement.executeUpdate(sql, columnIndexes);
		}

		public int executeUpdate(String sql, String[] columnNames) throws SQLException {
			return this.statement.executeUpdate(sql, columnNames);
		}

		public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
			return this.statement.execute(sql, autoGeneratedKeys);
		}

		public boolean execute(String sql, int[] columnIndexes) throws SQLException {
			return this.statement.execute(sql, columnIndexes);
		}

		public boolean execute(String sql, String[] columnNames) throws SQLException {
			return this.statement.execute(sql, columnNames);
		}

		public int getResultSetHoldability() throws SQLException {
			return this.statement.getResultSetHoldability();
		}

		public boolean isClosed() throws SQLException {
			return this.statement.isClosed();
		}

		public void setPoolable(boolean poolable) throws SQLException {
			this.statement.setPoolable(poolable);
		}

		public boolean isPoolable() throws SQLException {
			return this.statement.isPoolable();
		}

		public void closeOnCompletion() throws SQLException {
			this.statement.closeOnCompletion();
		}

		public boolean isCloseOnCompletion() throws SQLException {
			return this.statement.isCloseOnCompletion();
		}
	}

	class PreparedStatementProxy extends SQLConnectionProxy.StatementProxy implements PreparedStatement {
		PreparedStatement innerPreparedStatement;

		public PreparedStatementProxy(PreparedStatement preparedStatement) {
			super(preparedStatement);
			this.innerPreparedStatement = preparedStatement;
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return this.innerPreparedStatement.unwrap(iface);
		}

		public ResultSet executeQuery(String sql) throws SQLException {
			return this.innerPreparedStatement.executeQuery(sql);
		}

		public ResultSet executeQuery() throws SQLException {
			return this.innerPreparedStatement.executeQuery();
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return this.innerPreparedStatement.isWrapperFor(iface);
		}

		public int executeUpdate(String sql) throws SQLException {
			return this.innerPreparedStatement.executeUpdate(sql);
		}

		public int executeUpdate() throws SQLException {
			return this.innerPreparedStatement.executeUpdate();
		}

		public void setNull(int parameterIndex, int sqlType) throws SQLException {
			this.innerPreparedStatement.setNull(parameterIndex, sqlType);
		}

		public void close() throws SQLException {
			this.innerPreparedStatement.close();
		}

		public int getMaxFieldSize() throws SQLException {
			return this.innerPreparedStatement.getMaxFieldSize();
		}

		public void setBoolean(int parameterIndex, boolean x) throws SQLException {
			this.innerPreparedStatement.setBoolean(parameterIndex, x);
		}

		public void setByte(int parameterIndex, byte x) throws SQLException {
			this.innerPreparedStatement.setByte(parameterIndex, x);
		}

		public void setMaxFieldSize(int max) throws SQLException {
			this.innerPreparedStatement.setMaxFieldSize(max);
		}

		public void setShort(int parameterIndex, short x) throws SQLException {
			this.innerPreparedStatement.setShort(parameterIndex, x);
		}

		public int getMaxRows() throws SQLException {
			return this.innerPreparedStatement.getMaxRows();
		}

		public void setInt(int parameterIndex, int x) throws SQLException {
			this.innerPreparedStatement.setInt(parameterIndex, x);
		}

		public void setMaxRows(int max) throws SQLException {
			this.innerPreparedStatement.setMaxRows(max);
		}

		public void setLong(int parameterIndex, long x) throws SQLException {
			this.innerPreparedStatement.setLong(parameterIndex, x);
		}

		public void setEscapeProcessing(boolean enable) throws SQLException {
			this.innerPreparedStatement.setEscapeProcessing(enable);
		}

		public void setFloat(int parameterIndex, float x) throws SQLException {
			this.innerPreparedStatement.setFloat(parameterIndex, x);
		}

		public void setDouble(int parameterIndex, double x) throws SQLException {
			this.innerPreparedStatement.setDouble(parameterIndex, x);
		}

		public int getQueryTimeout() throws SQLException {
			return this.innerPreparedStatement.getQueryTimeout();
		}

		public void setQueryTimeout(int seconds) throws SQLException {
			this.innerPreparedStatement.setQueryTimeout(seconds);
		}

		public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
			this.innerPreparedStatement.setBigDecimal(parameterIndex, x);
		}

		public void setString(int parameterIndex, String x) throws SQLException {
			this.innerPreparedStatement.setString(parameterIndex, x);
		}

		public void setBytes(int parameterIndex, byte[] x) throws SQLException {
			this.innerPreparedStatement.setBytes(parameterIndex, x);
		}

		public void cancel() throws SQLException {
			this.innerPreparedStatement.cancel();
		}

		public SQLWarning getWarnings() throws SQLException {
			return this.innerPreparedStatement.getWarnings();
		}

		public void setDate(int parameterIndex, Date x) throws SQLException {
			this.innerPreparedStatement.setDate(parameterIndex, x);
		}

		public void setTime(int parameterIndex, Time x) throws SQLException {
			this.innerPreparedStatement.setTime(parameterIndex, x);
		}

		public void clearWarnings() throws SQLException {
			this.innerPreparedStatement.clearWarnings();
		}

		public void setCursorName(String name) throws SQLException {
			this.innerPreparedStatement.setCursorName(name);
		}

		public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
			this.innerPreparedStatement.setTimestamp(parameterIndex, x);
		}

		public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
			this.innerPreparedStatement.setAsciiStream(parameterIndex, x, length);
		}

		public boolean execute(String sql) throws SQLException {
			return this.innerPreparedStatement.execute(sql);
		}

		/**
		 * @deprecated
		 */
		public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
			this.innerPreparedStatement.setUnicodeStream(parameterIndex, x, length);
		}

		public ResultSet getResultSet() throws SQLException {
			return this.innerPreparedStatement.getResultSet();
		}

		public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
			this.innerPreparedStatement.setBinaryStream(parameterIndex, x, length);
		}

		public int getUpdateCount() throws SQLException {
			return this.innerPreparedStatement.getUpdateCount();
		}

		public boolean getMoreResults() throws SQLException {
			return this.innerPreparedStatement.getMoreResults();
		}

		public void clearParameters() throws SQLException {
			this.innerPreparedStatement.clearParameters();
		}

		public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
			this.innerPreparedStatement.setObject(parameterIndex, x, targetSqlType);
		}

		public void setFetchDirection(int direction) throws SQLException {
			this.innerPreparedStatement.setFetchDirection(direction);
		}

		public int getFetchDirection() throws SQLException {
			return this.innerPreparedStatement.getFetchDirection();
		}

		public void setObject(int parameterIndex, Object x) throws SQLException {
			this.innerPreparedStatement.setObject(parameterIndex, x);
		}

		public void setFetchSize(int rows) throws SQLException {
			this.innerPreparedStatement.setFetchSize(rows);
		}

		public int getFetchSize() throws SQLException {
			return this.innerPreparedStatement.getFetchSize();
		}

		public int getResultSetConcurrency() throws SQLException {
			return this.innerPreparedStatement.getResultSetConcurrency();
		}

		public boolean execute() throws SQLException {
			return this.innerPreparedStatement.execute();
		}

		public int getResultSetType() throws SQLException {
			return this.innerPreparedStatement.getResultSetType();
		}

		public void addBatch(String sql) throws SQLException {
			this.innerPreparedStatement.addBatch(sql);
		}

		public void clearBatch() throws SQLException {
			this.innerPreparedStatement.clearBatch();
		}

		public void addBatch() throws SQLException {
			this.innerPreparedStatement.addBatch();
		}

		public int[] executeBatch() throws SQLException {
			return this.innerPreparedStatement.executeBatch();
		}

		public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
			this.innerPreparedStatement.setCharacterStream(parameterIndex, reader, length);
		}

		public void setRef(int parameterIndex, Ref x) throws SQLException {
			this.innerPreparedStatement.setRef(parameterIndex, x);
		}

		public void setBlob(int parameterIndex, Blob x) throws SQLException {
			this.innerPreparedStatement.setBlob(parameterIndex, x);
		}

		public void setClob(int parameterIndex, Clob x) throws SQLException {
			this.innerPreparedStatement.setClob(parameterIndex, x);
		}

		public Connection getConnection() throws SQLException {
			return this.innerPreparedStatement.getConnection();
		}

		public void setArray(int parameterIndex, Array x) throws SQLException {
			this.innerPreparedStatement.setArray(parameterIndex, x);
		}

		public ResultSetMetaData getMetaData() throws SQLException {
			return this.innerPreparedStatement.getMetaData();
		}

		public boolean getMoreResults(int current) throws SQLException {
			return this.innerPreparedStatement.getMoreResults(current);
		}

		public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
			this.innerPreparedStatement.setDate(parameterIndex, x, cal);
		}

		public ResultSet getGeneratedKeys() throws SQLException {
			return this.innerPreparedStatement.getGeneratedKeys();
		}

		public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
			this.innerPreparedStatement.setTime(parameterIndex, x, cal);
		}

		public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
			return this.innerPreparedStatement.executeUpdate(sql, autoGeneratedKeys);
		}

		public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
			this.innerPreparedStatement.setTimestamp(parameterIndex, x, cal);
		}

		public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
			this.innerPreparedStatement.setNull(parameterIndex, sqlType, typeName);
		}

		public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
			return this.innerPreparedStatement.executeUpdate(sql, columnIndexes);
		}

		public void setURL(int parameterIndex, URL x) throws SQLException {
			this.innerPreparedStatement.setURL(parameterIndex, x);
		}

		public int executeUpdate(String sql, String[] columnNames) throws SQLException {
			return this.innerPreparedStatement.executeUpdate(sql, columnNames);
		}

		public ParameterMetaData getParameterMetaData() throws SQLException {
			return this.innerPreparedStatement.getParameterMetaData();
		}

		public void setRowId(int parameterIndex, RowId x) throws SQLException {
			this.innerPreparedStatement.setRowId(parameterIndex, x);
		}

		public void setNString(int parameterIndex, String value) throws SQLException {
			this.innerPreparedStatement.setNString(parameterIndex, value);
		}

		public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
			return this.innerPreparedStatement.execute(sql, autoGeneratedKeys);
		}

		public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
			this.innerPreparedStatement.setNCharacterStream(parameterIndex, value, length);
		}

		public void setNClob(int parameterIndex, NClob value) throws SQLException {
			this.innerPreparedStatement.setNClob(parameterIndex, value);
		}

		public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
			this.innerPreparedStatement.setClob(parameterIndex, reader, length);
		}

		public boolean execute(String sql, int[] columnIndexes) throws SQLException {
			return this.innerPreparedStatement.execute(sql, columnIndexes);
		}

		public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
			this.innerPreparedStatement.setBlob(parameterIndex, inputStream, length);
		}

		public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
			this.innerPreparedStatement.setNClob(parameterIndex, reader, length);
		}

		public boolean execute(String sql, String[] columnNames) throws SQLException {
			return this.innerPreparedStatement.execute(sql, columnNames);
		}

		public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
			this.innerPreparedStatement.setSQLXML(parameterIndex, xmlObject);
		}

		public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
			this.innerPreparedStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		}

		public int getResultSetHoldability() throws SQLException {
			return this.innerPreparedStatement.getResultSetHoldability();
		}

		public boolean isClosed() throws SQLException {
			return this.innerPreparedStatement.isClosed();
		}

		public void setPoolable(boolean poolable) throws SQLException {
			this.innerPreparedStatement.setPoolable(poolable);
		}

		public boolean isPoolable() throws SQLException {
			return this.innerPreparedStatement.isPoolable();
		}

		public void closeOnCompletion() throws SQLException {
			this.innerPreparedStatement.closeOnCompletion();
		}

		public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
			this.innerPreparedStatement.setAsciiStream(parameterIndex, x, length);
		}

		public boolean isCloseOnCompletion() throws SQLException {
			return this.innerPreparedStatement.isCloseOnCompletion();
		}

		public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
			this.innerPreparedStatement.setBinaryStream(parameterIndex, x, length);
		}

		public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
			this.innerPreparedStatement.setCharacterStream(parameterIndex, reader, length);
		}

		public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
			this.innerPreparedStatement.setAsciiStream(parameterIndex, x);
		}

		public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
			this.innerPreparedStatement.setBinaryStream(parameterIndex, x);
		}

		public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
			this.innerPreparedStatement.setCharacterStream(parameterIndex, reader);
		}

		public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
			this.innerPreparedStatement.setNCharacterStream(parameterIndex, value);
		}

		public void setClob(int parameterIndex, Reader reader) throws SQLException {
			this.innerPreparedStatement.setClob(parameterIndex, reader);
		}

		public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
			this.innerPreparedStatement.setBlob(parameterIndex, inputStream);
		}

		public void setNClob(int parameterIndex, Reader reader) throws SQLException {
			this.innerPreparedStatement.setNClob(parameterIndex, reader);
		}
	}

	class CallableStatementProxy extends SQLConnectionProxy.PreparedStatementProxy implements CallableStatement {
		CallableStatement innerCallableStatement;

		public CallableStatementProxy(CallableStatement callableStatement) {
			super(callableStatement);
			this.innerCallableStatement = callableStatement;
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return this.innerCallableStatement.unwrap(iface);
		}

		public ResultSet executeQuery(String sql) throws SQLException {
			return this.innerCallableStatement.executeQuery(sql);
		}

		public ResultSet executeQuery() throws SQLException {
			return this.innerCallableStatement.executeQuery();
		}

		public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
			this.innerCallableStatement.registerOutParameter(parameterIndex, sqlType);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return this.innerCallableStatement.isWrapperFor(iface);
		}

		public int executeUpdate(String sql) throws SQLException {
			return this.innerCallableStatement.executeUpdate(sql);
		}

		public int executeUpdate() throws SQLException {
			return this.innerCallableStatement.executeUpdate();
		}

		public void setNull(int parameterIndex, int sqlType) throws SQLException {
			this.innerCallableStatement.setNull(parameterIndex, sqlType);
		}

		public void close() throws SQLException {
			this.innerCallableStatement.close();
		}

		public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
			this.innerCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
		}

		public int getMaxFieldSize() throws SQLException {
			return this.innerCallableStatement.getMaxFieldSize();
		}

		public void setBoolean(int parameterIndex, boolean x) throws SQLException {
			this.innerCallableStatement.setBoolean(parameterIndex, x);
		}

		public void setByte(int parameterIndex, byte x) throws SQLException {
			this.innerCallableStatement.setByte(parameterIndex, x);
		}

		public void setMaxFieldSize(int max) throws SQLException {
			this.innerCallableStatement.setMaxFieldSize(max);
		}

		public boolean wasNull() throws SQLException {
			return this.innerCallableStatement.wasNull();
		}

		public void setShort(int parameterIndex, short x) throws SQLException {
			this.innerCallableStatement.setShort(parameterIndex, x);
		}

		public String getString(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getString(parameterIndex);
		}

		public int getMaxRows() throws SQLException {
			return this.innerCallableStatement.getMaxRows();
		}

		public void setInt(int parameterIndex, int x) throws SQLException {
			this.innerCallableStatement.setInt(parameterIndex, x);
		}

		public void setMaxRows(int max) throws SQLException {
			this.innerCallableStatement.setMaxRows(max);
		}

		public void setLong(int parameterIndex, long x) throws SQLException {
			this.innerCallableStatement.setLong(parameterIndex, x);
		}

		public boolean getBoolean(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getBoolean(parameterIndex);
		}

		public void setEscapeProcessing(boolean enable) throws SQLException {
			this.innerCallableStatement.setEscapeProcessing(enable);
		}

		public void setFloat(int parameterIndex, float x) throws SQLException {
			this.innerCallableStatement.setFloat(parameterIndex, x);
		}

		public byte getByte(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getByte(parameterIndex);
		}

		public void setDouble(int parameterIndex, double x) throws SQLException {
			this.innerCallableStatement.setDouble(parameterIndex, x);
		}

		public int getQueryTimeout() throws SQLException {
			return this.innerCallableStatement.getQueryTimeout();
		}

		public short getShort(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getShort(parameterIndex);
		}

		public void setQueryTimeout(int seconds) throws SQLException {
			this.innerCallableStatement.setQueryTimeout(seconds);
		}

		public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
			this.innerCallableStatement.setBigDecimal(parameterIndex, x);
		}

		public int getInt(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getInt(parameterIndex);
		}

		public void setString(int parameterIndex, String x) throws SQLException {
			this.innerCallableStatement.setString(parameterIndex, x);
		}

		public long getLong(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getLong(parameterIndex);
		}

		public void setBytes(int parameterIndex, byte[] x) throws SQLException {
			this.innerCallableStatement.setBytes(parameterIndex, x);
		}

		public float getFloat(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getFloat(parameterIndex);
		}

		public void cancel() throws SQLException {
			this.innerCallableStatement.cancel();
		}

		public SQLWarning getWarnings() throws SQLException {
			return this.innerCallableStatement.getWarnings();
		}

		public double getDouble(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getDouble(parameterIndex);
		}

		public void setDate(int parameterIndex, Date x) throws SQLException {
			this.innerCallableStatement.setDate(parameterIndex, x);
		}

		/**
		 * @deprecated
		 */
		public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
			return this.innerCallableStatement.getBigDecimal(parameterIndex, scale);
		}

		public void setTime(int parameterIndex, Time x) throws SQLException {
			this.innerCallableStatement.setTime(parameterIndex, x);
		}

		public void clearWarnings() throws SQLException {
			this.innerCallableStatement.clearWarnings();
		}

		public void setCursorName(String name) throws SQLException {
			this.innerCallableStatement.setCursorName(name);
		}

		public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
			this.innerCallableStatement.setTimestamp(parameterIndex, x);
		}

		public byte[] getBytes(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getBytes(parameterIndex);
		}

		public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
			this.innerCallableStatement.setAsciiStream(parameterIndex, x, length);
		}

		public Date getDate(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getDate(parameterIndex);
		}

		public boolean execute(String sql) throws SQLException {
			return this.innerCallableStatement.execute(sql);
		}

		public Time getTime(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getTime(parameterIndex);
		}

		/**
		 * @deprecated
		 */
		public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
			this.innerCallableStatement.setUnicodeStream(parameterIndex, x, length);
		}

		public Timestamp getTimestamp(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getTimestamp(parameterIndex);
		}

		public Object getObject(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getObject(parameterIndex);
		}

		public ResultSet getResultSet() throws SQLException {
			return this.innerCallableStatement.getResultSet();
		}

		public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
			this.innerCallableStatement.setBinaryStream(parameterIndex, x, length);
		}

		public int getUpdateCount() throws SQLException {
			return this.innerCallableStatement.getUpdateCount();
		}

		public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getBigDecimal(parameterIndex);
		}

		public boolean getMoreResults() throws SQLException {
			return this.innerCallableStatement.getMoreResults();
		}

		public void clearParameters() throws SQLException {
			this.innerCallableStatement.clearParameters();
		}

		public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
			return this.innerCallableStatement.getObject(parameterIndex, map);
		}

		public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
			this.innerCallableStatement.setObject(parameterIndex, x, targetSqlType);
		}

		public void setFetchDirection(int direction) throws SQLException {
			this.innerCallableStatement.setFetchDirection(direction);
		}

		public Ref getRef(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getRef(parameterIndex);
		}

		public int getFetchDirection() throws SQLException {
			return this.innerCallableStatement.getFetchDirection();
		}

		public void setObject(int parameterIndex, Object x) throws SQLException {
			this.innerCallableStatement.setObject(parameterIndex, x);
		}

		public Blob getBlob(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getBlob(parameterIndex);
		}

		public void setFetchSize(int rows) throws SQLException {
			this.innerCallableStatement.setFetchSize(rows);
		}

		public Clob getClob(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getClob(parameterIndex);
		}

		public int getFetchSize() throws SQLException {
			return this.innerCallableStatement.getFetchSize();
		}

		public int getResultSetConcurrency() throws SQLException {
			return this.innerCallableStatement.getResultSetConcurrency();
		}

		public Array getArray(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getArray(parameterIndex);
		}

		public boolean execute() throws SQLException {
			return this.innerCallableStatement.execute();
		}

		public int getResultSetType() throws SQLException {
			return this.innerCallableStatement.getResultSetType();
		}

		public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
			return this.innerCallableStatement.getDate(parameterIndex, cal);
		}

		public void addBatch(String sql) throws SQLException {
			this.innerCallableStatement.addBatch(sql);
		}

		public void clearBatch() throws SQLException {
			this.innerCallableStatement.clearBatch();
		}

		public void addBatch() throws SQLException {
			this.innerCallableStatement.addBatch();
		}

		public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
			return this.innerCallableStatement.getTime(parameterIndex, cal);
		}

		public int[] executeBatch() throws SQLException {
			return this.innerCallableStatement.executeBatch();
		}

		public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
			this.innerCallableStatement.setCharacterStream(parameterIndex, reader, length);
		}

		public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
			return this.innerCallableStatement.getTimestamp(parameterIndex, cal);
		}

		public void setRef(int parameterIndex, Ref x) throws SQLException {
			this.innerCallableStatement.setRef(parameterIndex, x);
		}

		public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
			this.innerCallableStatement.registerOutParameter(parameterIndex, sqlType, typeName);
		}

		public void setBlob(int parameterIndex, Blob x) throws SQLException {
			this.innerCallableStatement.setBlob(parameterIndex, x);
		}

		public void setClob(int parameterIndex, Clob x) throws SQLException {
			this.innerCallableStatement.setClob(parameterIndex, x);
		}

		public Connection getConnection() throws SQLException {
			return this.innerCallableStatement.getConnection();
		}

		public void setArray(int parameterIndex, Array x) throws SQLException {
			this.innerCallableStatement.setArray(parameterIndex, x);
		}

		public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
			this.innerCallableStatement.registerOutParameter(parameterName, sqlType);
		}

		public ResultSetMetaData getMetaData() throws SQLException {
			return this.innerCallableStatement.getMetaData();
		}

		public boolean getMoreResults(int current) throws SQLException {
			return this.innerCallableStatement.getMoreResults(current);
		}

		public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
			this.innerCallableStatement.setDate(parameterIndex, x, cal);
		}

		public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
			this.innerCallableStatement.registerOutParameter(parameterName, sqlType, scale);
		}

		public ResultSet getGeneratedKeys() throws SQLException {
			return this.innerCallableStatement.getGeneratedKeys();
		}

		public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
			this.innerCallableStatement.setTime(parameterIndex, x, cal);
		}

		public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
			return this.innerCallableStatement.executeUpdate(sql, autoGeneratedKeys);
		}

		public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
			this.innerCallableStatement.registerOutParameter(parameterName, sqlType, typeName);
		}

		public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
			this.innerCallableStatement.setTimestamp(parameterIndex, x, cal);
		}

		public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
			this.innerCallableStatement.setNull(parameterIndex, sqlType, typeName);
		}

		public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
			return this.innerCallableStatement.executeUpdate(sql, columnIndexes);
		}

		public URL getURL(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getURL(parameterIndex);
		}

		public void setURL(String parameterName, URL val) throws SQLException {
			this.innerCallableStatement.setURL(parameterName, val);
		}

		public void setURL(int parameterIndex, URL x) throws SQLException {
			this.innerCallableStatement.setURL(parameterIndex, x);
		}

		public void setNull(String parameterName, int sqlType) throws SQLException {
			this.innerCallableStatement.setNull(parameterName, sqlType);
		}

		public int executeUpdate(String sql, String[] columnNames) throws SQLException {
			return this.innerCallableStatement.executeUpdate(sql, columnNames);
		}

		public ParameterMetaData getParameterMetaData() throws SQLException {
			return this.innerCallableStatement.getParameterMetaData();
		}

		public void setBoolean(String parameterName, boolean x) throws SQLException {
			this.innerCallableStatement.setBoolean(parameterName, x);
		}

		public void setRowId(int parameterIndex, RowId x) throws SQLException {
			this.innerCallableStatement.setRowId(parameterIndex, x);
		}

		public void setByte(String parameterName, byte x) throws SQLException {
			this.innerCallableStatement.setByte(parameterName, x);
		}

		public void setNString(int parameterIndex, String value) throws SQLException {
			this.innerCallableStatement.setNString(parameterIndex, value);
		}

		public void setShort(String parameterName, short x) throws SQLException {
			this.innerCallableStatement.setShort(parameterName, x);
		}

		public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
			return this.innerCallableStatement.execute(sql, autoGeneratedKeys);
		}

		public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
			this.innerCallableStatement.setNCharacterStream(parameterIndex, value, length);
		}

		public void setInt(String parameterName, int x) throws SQLException {
			this.innerCallableStatement.setInt(parameterName, x);
		}

		public void setLong(String parameterName, long x) throws SQLException {
			this.innerCallableStatement.setLong(parameterName, x);
		}

		public void setNClob(int parameterIndex, NClob value) throws SQLException {
			this.innerCallableStatement.setNClob(parameterIndex, value);
		}

		public void setFloat(String parameterName, float x) throws SQLException {
			this.innerCallableStatement.setFloat(parameterName, x);
		}

		public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
			this.innerCallableStatement.setClob(parameterIndex, reader, length);
		}

		public void setDouble(String parameterName, double x) throws SQLException {
			this.innerCallableStatement.setDouble(parameterName, x);
		}

		public boolean execute(String sql, int[] columnIndexes) throws SQLException {
			return this.innerCallableStatement.execute(sql, columnIndexes);
		}

		public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
			this.innerCallableStatement.setBigDecimal(parameterName, x);
		}

		public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
			this.innerCallableStatement.setBlob(parameterIndex, inputStream, length);
		}

		public void setString(String parameterName, String x) throws SQLException {
			this.innerCallableStatement.setString(parameterName, x);
		}

		public void setBytes(String parameterName, byte[] x) throws SQLException {
			this.innerCallableStatement.setBytes(parameterName, x);
		}

		public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
			this.innerCallableStatement.setNClob(parameterIndex, reader, length);
		}

		public boolean execute(String sql, String[] columnNames) throws SQLException {
			return this.innerCallableStatement.execute(sql, columnNames);
		}

		public void setDate(String parameterName, Date x) throws SQLException {
			this.innerCallableStatement.setDate(parameterName, x);
		}

		public void setTime(String parameterName, Time x) throws SQLException {
			this.innerCallableStatement.setTime(parameterName, x);
		}

		public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
			this.innerCallableStatement.setSQLXML(parameterIndex, xmlObject);
		}

		public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
			this.innerCallableStatement.setTimestamp(parameterName, x);
		}

		public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
			this.innerCallableStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		}

		public int getResultSetHoldability() throws SQLException {
			return this.innerCallableStatement.getResultSetHoldability();
		}

		public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
			this.innerCallableStatement.setAsciiStream(parameterName, x, length);
		}

		public boolean isClosed() throws SQLException {
			return this.innerCallableStatement.isClosed();
		}

		public void setPoolable(boolean poolable) throws SQLException {
			this.innerCallableStatement.setPoolable(poolable);
		}

		public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
			this.innerCallableStatement.setBinaryStream(parameterName, x, length);
		}

		public boolean isPoolable() throws SQLException {
			return this.innerCallableStatement.isPoolable();
		}

		public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
			this.innerCallableStatement.setObject(parameterName, x, targetSqlType, scale);
		}

		public void closeOnCompletion() throws SQLException {
			this.innerCallableStatement.closeOnCompletion();
		}

		public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
			this.innerCallableStatement.setAsciiStream(parameterIndex, x, length);
		}

		public boolean isCloseOnCompletion() throws SQLException {
			return this.innerCallableStatement.isCloseOnCompletion();
		}

		public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
			this.innerCallableStatement.setBinaryStream(parameterIndex, x, length);
		}

		public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
			this.innerCallableStatement.setObject(parameterName, x, targetSqlType);
		}

		public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
			this.innerCallableStatement.setCharacterStream(parameterIndex, reader, length);
		}

		public void setObject(String parameterName, Object x) throws SQLException {
			this.innerCallableStatement.setObject(parameterName, x);
		}

		public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
			this.innerCallableStatement.setAsciiStream(parameterIndex, x);
		}

		public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
			this.innerCallableStatement.setBinaryStream(parameterIndex, x);
		}

		public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
			this.innerCallableStatement.setCharacterStream(parameterName, reader, length);
		}

		public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
			this.innerCallableStatement.setCharacterStream(parameterIndex, reader);
		}

		public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
			this.innerCallableStatement.setDate(parameterName, x, cal);
		}

		public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
			this.innerCallableStatement.setNCharacterStream(parameterIndex, value);
		}

		public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
			this.innerCallableStatement.setTime(parameterName, x, cal);
		}

		public void setClob(int parameterIndex, Reader reader) throws SQLException {
			this.innerCallableStatement.setClob(parameterIndex, reader);
		}

		public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
			this.innerCallableStatement.setTimestamp(parameterName, x, cal);
		}

		public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
			this.innerCallableStatement.setNull(parameterName, sqlType, typeName);
		}

		public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
			this.innerCallableStatement.setBlob(parameterIndex, inputStream);
		}

		public void setNClob(int parameterIndex, Reader reader) throws SQLException {
			this.innerCallableStatement.setNClob(parameterIndex, reader);
		}

		public String getString(String parameterName) throws SQLException {
			return this.innerCallableStatement.getString(parameterName);
		}

		public boolean getBoolean(String parameterName) throws SQLException {
			return this.innerCallableStatement.getBoolean(parameterName);
		}

		public byte getByte(String parameterName) throws SQLException {
			return this.innerCallableStatement.getByte(parameterName);
		}

		public short getShort(String parameterName) throws SQLException {
			return this.innerCallableStatement.getShort(parameterName);
		}

		public int getInt(String parameterName) throws SQLException {
			return this.innerCallableStatement.getInt(parameterName);
		}

		public long getLong(String parameterName) throws SQLException {
			return this.innerCallableStatement.getLong(parameterName);
		}

		public float getFloat(String parameterName) throws SQLException {
			return this.innerCallableStatement.getFloat(parameterName);
		}

		public double getDouble(String parameterName) throws SQLException {
			return this.innerCallableStatement.getDouble(parameterName);
		}

		public byte[] getBytes(String parameterName) throws SQLException {
			return this.innerCallableStatement.getBytes(parameterName);
		}

		public Date getDate(String parameterName) throws SQLException {
			return this.innerCallableStatement.getDate(parameterName);
		}

		public Time getTime(String parameterName) throws SQLException {
			return this.innerCallableStatement.getTime(parameterName);
		}

		public Timestamp getTimestamp(String parameterName) throws SQLException {
			return this.innerCallableStatement.getTimestamp(parameterName);
		}

		public Object getObject(String parameterName) throws SQLException {
			return this.innerCallableStatement.getObject(parameterName);
		}

		public BigDecimal getBigDecimal(String parameterName) throws SQLException {
			return this.innerCallableStatement.getBigDecimal(parameterName);
		}

		public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
			return this.innerCallableStatement.getObject(parameterName, map);
		}

		public Ref getRef(String parameterName) throws SQLException {
			return this.innerCallableStatement.getRef(parameterName);
		}

		public Blob getBlob(String parameterName) throws SQLException {
			return this.innerCallableStatement.getBlob(parameterName);
		}

		public Clob getClob(String parameterName) throws SQLException {
			return this.innerCallableStatement.getClob(parameterName);
		}

		public Array getArray(String parameterName) throws SQLException {
			return this.innerCallableStatement.getArray(parameterName);
		}

		public Date getDate(String parameterName, Calendar cal) throws SQLException {
			return this.innerCallableStatement.getDate(parameterName, cal);
		}

		public Time getTime(String parameterName, Calendar cal) throws SQLException {
			return this.innerCallableStatement.getTime(parameterName, cal);
		}

		public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
			return this.innerCallableStatement.getTimestamp(parameterName, cal);
		}

		public URL getURL(String parameterName) throws SQLException {
			return this.innerCallableStatement.getURL(parameterName);
		}

		public RowId getRowId(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getRowId(parameterIndex);
		}

		public RowId getRowId(String parameterName) throws SQLException {
			return this.innerCallableStatement.getRowId(parameterName);
		}

		public void setRowId(String parameterName, RowId x) throws SQLException {
			this.innerCallableStatement.setRowId(parameterName, x);
		}

		public void setNString(String parameterName, String value) throws SQLException {
			this.innerCallableStatement.setNString(parameterName, value);
		}

		public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
			this.innerCallableStatement.setNCharacterStream(parameterName, value, length);
		}

		public void setNClob(String parameterName, NClob value) throws SQLException {
			this.innerCallableStatement.setNClob(parameterName, value);
		}

		public void setClob(String parameterName, Reader reader, long length) throws SQLException {
			this.innerCallableStatement.setClob(parameterName, reader, length);
		}

		public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
			this.innerCallableStatement.setBlob(parameterName, inputStream, length);
		}

		public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
			this.innerCallableStatement.setNClob(parameterName, reader, length);
		}

		public NClob getNClob(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getNClob(parameterIndex);
		}

		public NClob getNClob(String parameterName) throws SQLException {
			return this.innerCallableStatement.getNClob(parameterName);
		}

		public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
			this.innerCallableStatement.setSQLXML(parameterName, xmlObject);
		}

		public SQLXML getSQLXML(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getSQLXML(parameterIndex);
		}

		public SQLXML getSQLXML(String parameterName) throws SQLException {
			return this.innerCallableStatement.getSQLXML(parameterName);
		}

		public String getNString(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getNString(parameterIndex);
		}

		public String getNString(String parameterName) throws SQLException {
			return this.innerCallableStatement.getNString(parameterName);
		}

		public Reader getNCharacterStream(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getNCharacterStream(parameterIndex);
		}

		public Reader getNCharacterStream(String parameterName) throws SQLException {
			return this.innerCallableStatement.getNCharacterStream(parameterName);
		}

		public Reader getCharacterStream(int parameterIndex) throws SQLException {
			return this.innerCallableStatement.getCharacterStream(parameterIndex);
		}

		public Reader getCharacterStream(String parameterName) throws SQLException {
			return this.innerCallableStatement.getCharacterStream(parameterName);
		}

		public void setBlob(String parameterName, Blob x) throws SQLException {
			this.innerCallableStatement.setBlob(parameterName, x);
		}

		public void setClob(String parameterName, Clob x) throws SQLException {
			this.innerCallableStatement.setClob(parameterName, x);
		}

		public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
			this.innerCallableStatement.setAsciiStream(parameterName, x, length);
		}

		public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
			this.innerCallableStatement.setBinaryStream(parameterName, x, length);
		}

		public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
			this.innerCallableStatement.setCharacterStream(parameterName, reader, length);
		}

		public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
			this.innerCallableStatement.setAsciiStream(parameterName, x);
		}

		public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
			this.innerCallableStatement.setBinaryStream(parameterName, x);
		}

		public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
			this.innerCallableStatement.setCharacterStream(parameterName, reader);
		}

		public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
			this.innerCallableStatement.setNCharacterStream(parameterName, value);
		}

		public void setClob(String parameterName, Reader reader) throws SQLException {
			this.innerCallableStatement.setClob(parameterName, reader);
		}

		public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
			this.innerCallableStatement.setBlob(parameterName, inputStream);
		}

		public void setNClob(String parameterName, Reader reader) throws SQLException {
			this.innerCallableStatement.setNClob(parameterName, reader);
		}

		public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
			return this.innerCallableStatement.getObject(parameterIndex, type);
		}

		public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
			return this.innerCallableStatement.getObject(parameterName, type);
		}
	}

	class ResultSetProxy implements ResultSet {
		ResultSet resultSet;

		public ResultSetProxy(ResultSet resultSet) {
			this.resultSet = resultSet;
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return this.resultSet.unwrap(iface);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return this.resultSet.isWrapperFor(iface);
		}

		public boolean next() throws SQLException {
			return this.resultSet.next();
		}

		public void close() throws SQLException {
			this.resultSet.close();
		}

		public boolean wasNull() throws SQLException {
			return this.resultSet.wasNull();
		}

		public String getString(int columnIndex) throws SQLException {
			return this.resultSet.getString(columnIndex);
		}

		public boolean getBoolean(int columnIndex) throws SQLException {
			return this.resultSet.getBoolean(columnIndex);
		}

		public byte getByte(int columnIndex) throws SQLException {
			return this.resultSet.getByte(columnIndex);
		}

		public short getShort(int columnIndex) throws SQLException {
			return this.resultSet.getShort(columnIndex);
		}

		public int getInt(int columnIndex) throws SQLException {
			return this.resultSet.getInt(columnIndex);
		}

		public long getLong(int columnIndex) throws SQLException {
			return this.resultSet.getLong(columnIndex);
		}

		public float getFloat(int columnIndex) throws SQLException {
			return this.resultSet.getFloat(columnIndex);
		}

		public double getDouble(int columnIndex) throws SQLException {
			return this.resultSet.getDouble(columnIndex);
		}

		@Deprecated
		public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
			BigDecimal bigDecimal = this.resultSet.getBigDecimal(columnIndex, scale);
			if (bigDecimal == null) {
				bigDecimal = new BigDecimal(0);
			}
			return bigDecimal;
		}

		public byte[] getBytes(int columnIndex) throws SQLException {
			return this.resultSet.getBytes(columnIndex);
		}

		public Date getDate(int columnIndex) throws SQLException {
			return this.resultSet.getDate(columnIndex);
		}

		public Time getTime(int columnIndex) throws SQLException {
			return this.resultSet.getTime(columnIndex);
		}

		public Timestamp getTimestamp(int columnIndex) throws SQLException {
			return this.resultSet.getTimestamp(columnIndex);
		}

		public InputStream getAsciiStream(int columnIndex) throws SQLException {
			return this.resultSet.getAsciiStream(columnIndex);
		}

		@Deprecated
		public InputStream getUnicodeStream(int columnIndex) throws SQLException {
			return this.resultSet.getUnicodeStream(columnIndex);
		}

		public InputStream getBinaryStream(int columnIndex) throws SQLException {
			return this.resultSet.getBinaryStream(columnIndex);
		}

		public String getString(String columnLabel) throws SQLException {
			return this.resultSet.getString(columnLabel);
		}

		public boolean getBoolean(String columnLabel) throws SQLException {
			return this.resultSet.getBoolean(columnLabel);
		}

		public byte getByte(String columnLabel) throws SQLException {
			return this.resultSet.getByte(columnLabel);
		}

		public short getShort(String columnLabel) throws SQLException {
			return this.resultSet.getShort(columnLabel);
		}

		public int getInt(String columnLabel) throws SQLException {
			return this.resultSet.getInt(columnLabel);
		}

		public long getLong(String columnLabel) throws SQLException {
			return this.resultSet.getLong(columnLabel);
		}

		public float getFloat(String columnLabel) throws SQLException {
			return this.resultSet.getFloat(columnLabel);
		}

		public double getDouble(String columnLabel) throws SQLException {
			return this.resultSet.getDouble(columnLabel);
		}

		@Deprecated
		public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
			BigDecimal bigDecimal = this.resultSet.getBigDecimal(columnLabel, scale);
			if (bigDecimal == null) {
				bigDecimal = new BigDecimal(0);
			}
			return bigDecimal;
		}

		public byte[] getBytes(String columnLabel) throws SQLException {
			return this.resultSet.getBytes(columnLabel);
		}

		public Date getDate(String columnLabel) throws SQLException {
			try {
				return this.resultSet.getDate(columnLabel);
			} catch (SQLException e) {
			}
			return null;
		}

		public Time getTime(String columnLabel) throws SQLException {
			try {
				return this.resultSet.getTime(columnLabel);
			} catch (SQLException e) {
			}
			return null;
		}

		public Timestamp getTimestamp(String columnLabel) throws SQLException {
			try {
				return this.resultSet.getTimestamp(columnLabel);
			} catch (SQLException e) {
			}
			return null;
		}

		public InputStream getAsciiStream(String columnLabel) throws SQLException {
			return this.resultSet.getAsciiStream(columnLabel);
		}

		@Deprecated
		public InputStream getUnicodeStream(String columnLabel) throws SQLException {
			return this.resultSet.getUnicodeStream(columnLabel);
		}

		public InputStream getBinaryStream(String columnLabel) throws SQLException {
			return this.resultSet.getBinaryStream(columnLabel);
		}

		public SQLWarning getWarnings() throws SQLException {
			return this.resultSet.getWarnings();
		}

		public void clearWarnings() throws SQLException {
			this.resultSet.clearWarnings();
		}

		public String getCursorName() throws SQLException {
			return this.resultSet.getCursorName();
		}

		public ResultSetMetaData getMetaData() throws SQLException {
			return this.resultSet.getMetaData();
		}

		public Object getObject(int columnIndex) throws SQLException {
			return this.resultSet.getObject(columnIndex);
		}

		public Object getObject(String columnLabel) throws SQLException {
			return this.resultSet.getObject(columnLabel);
		}

		public int findColumn(String columnLabel) throws SQLException {
			return this.resultSet.findColumn(columnLabel);
		}

		public Reader getCharacterStream(int columnIndex) throws SQLException {
			return this.resultSet.getCharacterStream(columnIndex);
		}

		public Reader getCharacterStream(String columnLabel) throws SQLException {
			return this.resultSet.getCharacterStream(columnLabel);
		}

		public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
			BigDecimal bigDecimal = this.resultSet.getBigDecimal(columnIndex);
			if (bigDecimal == null) {
				bigDecimal = new BigDecimal(0);
			}
			return bigDecimal;
		}

		public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
			BigDecimal bigDecimal = this.resultSet.getBigDecimal(columnLabel);
			if (bigDecimal == null) {
				bigDecimal = new BigDecimal(0);
			}
			return bigDecimal;
		}

		public boolean isBeforeFirst() throws SQLException {
			return this.resultSet.isBeforeFirst();
		}

		public boolean isAfterLast() throws SQLException {
			return this.resultSet.isAfterLast();
		}

		public boolean isFirst() throws SQLException {
			return this.resultSet.isFirst();
		}

		public boolean isLast() throws SQLException {
			return this.resultSet.isLast();
		}

		public void beforeFirst() throws SQLException {
			this.resultSet.beforeFirst();
		}

		public void afterLast() throws SQLException {
			this.resultSet.afterLast();
		}

		public boolean first() throws SQLException {
			return this.resultSet.first();
		}

		public boolean last() throws SQLException {
			return this.resultSet.last();
		}

		public int getRow() throws SQLException {
			return this.resultSet.getRow();
		}

		public boolean absolute(int row) throws SQLException {
			return this.resultSet.absolute(row);
		}

		public boolean relative(int rows) throws SQLException {
			return this.resultSet.relative(rows);
		}

		public boolean previous() throws SQLException {
			return this.resultSet.previous();
		}

		public void setFetchDirection(int direction) throws SQLException {
			this.resultSet.setFetchDirection(direction);
		}

		public int getFetchDirection() throws SQLException {
			return this.resultSet.getFetchDirection();
		}

		public void setFetchSize(int rows) throws SQLException {
			this.resultSet.setFetchSize(rows);
		}

		public int getFetchSize() throws SQLException {
			return this.resultSet.getFetchSize();
		}

		public int getType() throws SQLException {
			return this.resultSet.getType();
		}

		public int getConcurrency() throws SQLException {
			return this.resultSet.getConcurrency();
		}

		public boolean rowUpdated() throws SQLException {
			return this.resultSet.rowUpdated();
		}

		public boolean rowInserted() throws SQLException {
			return this.resultSet.rowInserted();
		}

		public boolean rowDeleted() throws SQLException {
			return this.resultSet.rowDeleted();
		}

		public void updateNull(int columnIndex) throws SQLException {
			this.resultSet.updateNull(columnIndex);
		}

		public void updateBoolean(int columnIndex, boolean x) throws SQLException {
			this.resultSet.updateBoolean(columnIndex, x);
		}

		public void updateByte(int columnIndex, byte x) throws SQLException {
			this.resultSet.updateByte(columnIndex, x);
		}

		public void updateShort(int columnIndex, short x) throws SQLException {
			this.resultSet.updateShort(columnIndex, x);
		}

		public void updateInt(int columnIndex, int x) throws SQLException {
			this.resultSet.updateInt(columnIndex, x);
		}

		public void updateLong(int columnIndex, long x) throws SQLException {
			this.resultSet.updateLong(columnIndex, x);
		}

		public void updateFloat(int columnIndex, float x) throws SQLException {
			this.resultSet.updateFloat(columnIndex, x);
		}

		public void updateDouble(int columnIndex, double x) throws SQLException {
			this.resultSet.updateDouble(columnIndex, x);
		}

		public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
			this.resultSet.updateBigDecimal(columnIndex, x);
		}

		public void updateString(int columnIndex, String x) throws SQLException {
			this.resultSet.updateString(columnIndex, x);
		}

		public void updateBytes(int columnIndex, byte[] x) throws SQLException {
			this.resultSet.updateBytes(columnIndex, x);
		}

		public void updateDate(int columnIndex, Date x) throws SQLException {
			this.resultSet.updateDate(columnIndex, x);
		}

		public void updateTime(int columnIndex, Time x) throws SQLException {
			this.resultSet.updateTime(columnIndex, x);
		}

		public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
			this.resultSet.updateTimestamp(columnIndex, x);
		}

		public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
			this.resultSet.updateAsciiStream(columnIndex, x, length);
		}

		public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
			this.resultSet.updateBinaryStream(columnIndex, x, length);
		}

		public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
			this.resultSet.updateCharacterStream(columnIndex, x, length);
		}

		public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
			this.resultSet.updateObject(columnIndex, x, scaleOrLength);
		}

		public void updateObject(int columnIndex, Object x) throws SQLException {
			this.resultSet.updateObject(columnIndex, x);
		}

		public void updateNull(String columnLabel) throws SQLException {
			this.resultSet.updateNull(columnLabel);
		}

		public void updateBoolean(String columnLabel, boolean x) throws SQLException {
			this.resultSet.updateBoolean(columnLabel, x);
		}

		public void updateByte(String columnLabel, byte x) throws SQLException {
			this.resultSet.updateByte(columnLabel, x);
		}

		public void updateShort(String columnLabel, short x) throws SQLException {
			this.resultSet.updateShort(columnLabel, x);
		}

		public void updateInt(String columnLabel, int x) throws SQLException {
			this.resultSet.updateInt(columnLabel, x);
		}

		public void updateLong(String columnLabel, long x) throws SQLException {
			this.resultSet.updateLong(columnLabel, x);
		}

		public void updateFloat(String columnLabel, float x) throws SQLException {
			this.resultSet.updateFloat(columnLabel, x);
		}

		public void updateDouble(String columnLabel, double x) throws SQLException {
			this.resultSet.updateDouble(columnLabel, x);
		}

		public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
			this.resultSet.updateBigDecimal(columnLabel, x);
		}

		public void updateString(String columnLabel, String x) throws SQLException {
			this.resultSet.updateString(columnLabel, x);
		}

		public void updateBytes(String columnLabel, byte[] x) throws SQLException {
			this.resultSet.updateBytes(columnLabel, x);
		}

		public void updateDate(String columnLabel, Date x) throws SQLException {
			this.resultSet.updateDate(columnLabel, x);
		}

		public void updateTime(String columnLabel, Time x) throws SQLException {
			this.resultSet.updateTime(columnLabel, x);
		}

		public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
			this.resultSet.updateTimestamp(columnLabel, x);
		}

		public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
			this.resultSet.updateAsciiStream(columnLabel, x, length);
		}

		public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
			this.resultSet.updateBinaryStream(columnLabel, x, length);
		}

		public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
			this.resultSet.updateCharacterStream(columnLabel, reader, length);
		}

		public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
			this.resultSet.updateObject(columnLabel, x, scaleOrLength);
		}

		public void updateObject(String columnLabel, Object x) throws SQLException {
			this.resultSet.updateObject(columnLabel, x);
		}

		public void insertRow() throws SQLException {
			this.resultSet.insertRow();
		}

		public void updateRow() throws SQLException {
			this.resultSet.updateRow();
		}

		public void deleteRow() throws SQLException {
			this.resultSet.deleteRow();
		}

		public void refreshRow() throws SQLException {
			this.resultSet.refreshRow();
		}

		public void cancelRowUpdates() throws SQLException {
			this.resultSet.cancelRowUpdates();
		}

		public void moveToInsertRow() throws SQLException {
			this.resultSet.moveToInsertRow();
		}

		public void moveToCurrentRow() throws SQLException {
			this.resultSet.moveToCurrentRow();
		}

		public Statement getStatement() throws SQLException {
			return this.resultSet.getStatement();
		}

		public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
			return this.resultSet.getObject(columnIndex, map);
		}

		public Ref getRef(int columnIndex) throws SQLException {
			return this.resultSet.getRef(columnIndex);
		}

		public Blob getBlob(int columnIndex) throws SQLException {
			return this.resultSet.getBlob(columnIndex);
		}

		public Clob getClob(int columnIndex) throws SQLException {
			return this.resultSet.getClob(columnIndex);
		}

		public Array getArray(int columnIndex) throws SQLException {
			return this.resultSet.getArray(columnIndex);
		}

		public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
			return this.resultSet.getObject(columnLabel, map);
		}

		public Ref getRef(String columnLabel) throws SQLException {
			return this.resultSet.getRef(columnLabel);
		}

		public Blob getBlob(String columnLabel) throws SQLException {
			return this.resultSet.getBlob(columnLabel);
		}

		public Clob getClob(String columnLabel) throws SQLException {
			return this.resultSet.getClob(columnLabel);
		}

		public Array getArray(String columnLabel) throws SQLException {
			return this.resultSet.getArray(columnLabel);
		}

		public Date getDate(int columnIndex, Calendar cal) throws SQLException {
			return this.resultSet.getDate(columnIndex, cal);
		}

		public Date getDate(String columnLabel, Calendar cal) throws SQLException {
			return this.resultSet.getDate(columnLabel, cal);
		}

		public Time getTime(int columnIndex, Calendar cal) throws SQLException {
			return this.resultSet.getTime(columnIndex, cal);
		}

		public Time getTime(String columnLabel, Calendar cal) throws SQLException {
			return this.resultSet.getTime(columnLabel, cal);
		}

		public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
			return this.resultSet.getTimestamp(columnIndex, cal);
		}

		public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
			return this.resultSet.getTimestamp(columnLabel, cal);
		}

		public URL getURL(int columnIndex) throws SQLException {
			return this.resultSet.getURL(columnIndex);
		}

		public URL getURL(String columnLabel) throws SQLException {
			return this.resultSet.getURL(columnLabel);
		}

		public void updateRef(int columnIndex, Ref x) throws SQLException {
			this.resultSet.updateRef(columnIndex, x);
		}

		public void updateRef(String columnLabel, Ref x) throws SQLException {
			this.resultSet.updateRef(columnLabel, x);
		}

		public void updateBlob(int columnIndex, Blob x) throws SQLException {
			this.resultSet.updateBlob(columnIndex, x);
		}

		public void updateBlob(String columnLabel, Blob x) throws SQLException {
			this.resultSet.updateBlob(columnLabel, x);
		}

		public void updateClob(int columnIndex, Clob x) throws SQLException {
			this.resultSet.updateClob(columnIndex, x);
		}

		public void updateClob(String columnLabel, Clob x) throws SQLException {
			this.resultSet.updateClob(columnLabel, x);
		}

		public void updateArray(int columnIndex, Array x) throws SQLException {
			this.resultSet.updateArray(columnIndex, x);
		}

		public void updateArray(String columnLabel, Array x) throws SQLException {
			this.resultSet.updateArray(columnLabel, x);
		}

		public RowId getRowId(int columnIndex) throws SQLException {
			return this.resultSet.getRowId(columnIndex);
		}

		public RowId getRowId(String columnLabel) throws SQLException {
			return this.resultSet.getRowId(columnLabel);
		}

		public void updateRowId(int columnIndex, RowId x) throws SQLException {
			this.resultSet.updateRowId(columnIndex, x);
		}

		public void updateRowId(String columnLabel, RowId x) throws SQLException {
			this.resultSet.updateRowId(columnLabel, x);
		}

		public int getHoldability() throws SQLException {
			return this.resultSet.getHoldability();
		}

		public boolean isClosed() throws SQLException {
			return this.resultSet.isClosed();
		}

		public void updateNString(int columnIndex, String nString) throws SQLException {
			this.resultSet.updateNString(columnIndex, nString);
		}

		public void updateNString(String columnLabel, String nString) throws SQLException {
			this.resultSet.updateNString(columnLabel, nString);
		}

		public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
			this.resultSet.updateNClob(columnIndex, nClob);
		}

		public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
			this.resultSet.updateNClob(columnLabel, nClob);
		}

		public NClob getNClob(int columnIndex) throws SQLException {
			return this.resultSet.getNClob(columnIndex);
		}

		public NClob getNClob(String columnLabel) throws SQLException {
			return this.resultSet.getNClob(columnLabel);
		}

		public SQLXML getSQLXML(int columnIndex) throws SQLException {
			return this.resultSet.getSQLXML(columnIndex);
		}

		public SQLXML getSQLXML(String columnLabel) throws SQLException {
			return this.resultSet.getSQLXML(columnLabel);
		}

		public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
			this.resultSet.updateSQLXML(columnIndex, xmlObject);
		}

		public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
			this.resultSet.updateSQLXML(columnLabel, xmlObject);
		}

		public String getNString(int columnIndex) throws SQLException {
			return this.resultSet.getNString(columnIndex);
		}

		public String getNString(String columnLabel) throws SQLException {
			return this.resultSet.getNString(columnLabel);
		}

		public Reader getNCharacterStream(int columnIndex) throws SQLException {
			return this.resultSet.getNCharacterStream(columnIndex);
		}

		public Reader getNCharacterStream(String columnLabel) throws SQLException {
			return this.resultSet.getNCharacterStream(columnLabel);
		}

		public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
			this.resultSet.updateNCharacterStream(columnIndex, x, length);
		}

		public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
			this.resultSet.updateNCharacterStream(columnLabel, reader, length);
		}

		public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
			this.resultSet.updateAsciiStream(columnIndex, x, length);
		}

		public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
			this.resultSet.updateBinaryStream(columnIndex, x, length);
		}

		public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
			this.resultSet.updateCharacterStream(columnIndex, x, length);
		}

		public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
			this.resultSet.updateAsciiStream(columnLabel, x, length);
		}

		public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
			this.resultSet.updateBinaryStream(columnLabel, x, length);
		}

		public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
			this.resultSet.updateCharacterStream(columnLabel, reader, length);
		}

		public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
			this.resultSet.updateBlob(columnIndex, inputStream, length);
		}

		public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
			this.resultSet.updateBlob(columnLabel, inputStream, length);
		}

		public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
			this.resultSet.updateClob(columnIndex, reader, length);
		}

		public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
			this.resultSet.updateClob(columnLabel, reader, length);
		}

		public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
			this.resultSet.updateNClob(columnIndex, reader, length);
		}

		public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
			this.resultSet.updateNClob(columnLabel, reader, length);
		}

		public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
			this.resultSet.updateNCharacterStream(columnIndex, x);
		}

		public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
			this.resultSet.updateNCharacterStream(columnLabel, reader);
		}

		public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
			this.resultSet.updateAsciiStream(columnIndex, x);
		}

		public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
			this.resultSet.updateBinaryStream(columnIndex, x);
		}

		public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
			this.resultSet.updateCharacterStream(columnIndex, x);
		}

		public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
			this.resultSet.updateAsciiStream(columnLabel, x);
		}

		public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
			this.resultSet.updateBinaryStream(columnLabel, x);
		}

		public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
			this.resultSet.updateCharacterStream(columnLabel, reader);
		}

		public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
			this.resultSet.updateBlob(columnIndex, inputStream);
		}

		public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
			this.resultSet.updateBlob(columnLabel, inputStream);
		}

		public void updateClob(int columnIndex, Reader reader) throws SQLException {
			this.resultSet.updateClob(columnIndex, reader);
		}

		public void updateClob(String columnLabel, Reader reader) throws SQLException {
			this.resultSet.updateClob(columnLabel, reader);
		}

		public void updateNClob(int columnIndex, Reader reader) throws SQLException {
			this.resultSet.updateNClob(columnIndex, reader);
		}

		public void updateNClob(String columnLabel, Reader reader) throws SQLException {
			this.resultSet.updateNClob(columnLabel, reader);
		}

		public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
			return this.resultSet.getObject(columnIndex, type);
		}

		public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
			return this.resultSet.getObject(columnLabel, type);
		}
	}
}