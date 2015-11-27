package org.luisyang.framework.config.achieve.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.config.entity.VariableBean;
import org.luisyang.framework.config.entity.VariableType;
import org.luisyang.framework.config.service.VariableManage;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.framework.service.query.ArrayParser;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;
import org.luisyang.util.StringHelper;

public class VariableManageImpl extends AbstractConfigService implements VariableManage {
	public static class RightManageFactory implements ServiceFactory<VariableManage> {
		public VariableManage newInstance(ServiceResource serviceResource) {
			return new VariableManageImpl(serviceResource);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final ArrayParser<VariableBean> ARRAY_PARSER = new ArrayParser() {
		public VariableBean[] parse(ResultSet resultSet) throws SQLException {
			List<VariableBean> variableBeans = null;
			while (resultSet.next()) {
				if (variableBeans == null) {
					variableBeans = new ArrayList();
				}
				VariableBean variableBean = new VariableManageImpl.VariableBeanImpl(resultSet.getString(1),
						resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), true);

				variableBeans.add(variableBean);
			}
			return variableBeans == null ? new VariableBean[0]
					: (VariableBean[]) variableBeans.toArray(new VariableBean[variableBeans.size()]);
		}
	};

	public VariableManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/**
	 * 获得属性值
	 */
	public String getProperty(String key) throws Throwable {
		if (StringHelper.isEmpty(key)) {
			return null;
		}
		String value = null;
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1010 WHERE F01 = ?");
			try {
				pstmt.setString(1, key);
				ResultSet resultSet = pstmt.executeQuery();
				try {
					if (resultSet.next()) {
						value = resultSet.getString(1);
					}
				} catch (Throwable localThrowable1) {
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				throw localThrowable2;
			} finally {
			}
		} catch (Throwable localThrowable3) {
			localThrowable4 = localThrowable3;
			throw localThrowable3;
		} finally {
			if (connection != null) {
				if (localThrowable4 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable4.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
		return value;
	}

	/**
	 * 更新属性值
	 */
	public void setProperty(String key, String value) throws Throwable {
		if ((StringHelper.isEmpty(key)) || (StringHelper.isEmpty(value))) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable3 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE _1010 SET F02 = ? WHERE F01 = ?");
			try {
				pstmt.setString(1, value);
				pstmt.setString(2, key);
				pstmt.executeUpdate();
			} catch (Throwable localThrowable1) {
				throw localThrowable1;
			} finally {
			}
		} catch (Throwable localThrowable2) {
			localThrowable3 = localThrowable2;
			throw localThrowable2;
		} finally {
			if (connection != null) {
				if (localThrowable3 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable3.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
		((ConfigureProvider) this.serviceResource.getResource(ConfigureProvider.class)).invalidProperty(key);
	}

	/**
	 * 分页搜索属性集合
	 */
	public PagingResult<VariableBean> search(VariableManage.VariableQuery variableQuery, Paging paging)
			throws Throwable {
		StringBuilder builder = new StringBuilder("SELECT F01, F02, F03, F04 FROM _1010 WHERE 1 = 1 ");

		ArrayList<Object> parameters = new ArrayList();
		SQLConnectionProvider connectionProvider = getConnectionProvider();
		if (variableQuery != null) {
			if (!StringHelper.isEmpty(variableQuery.getKey())) {
				builder.append(" AND F01 LIKE ?");
				parameters.add(connectionProvider.allMatch(variableQuery.getKey()));
			}
			if (!StringHelper.isEmpty(variableQuery.getValue())) {
				builder.append(" AND F02 LIKE ?");
				parameters.add(connectionProvider.allMatch(variableQuery.getValue()));
			}
			if (!StringHelper.isEmpty(variableQuery.getType())) {
				builder.append(" AND F03 = ?");
				parameters.add(variableQuery.getType());
			}
			if (!StringHelper.isEmpty(variableQuery.getDescription())) {
				builder.append(" AND F04 LIKE ?");
				parameters.add(connectionProvider.allMatch(variableQuery.getDescription()));
			}
		}
		Connection connection = getConnection();
		Throwable localThrowable2 = null;
		try {
			return selectPaging(connection, ARRAY_PARSER, paging, builder.toString(), parameters);
		} catch (Throwable localThrowable3) {
			localThrowable2 = localThrowable3;
			throw localThrowable3;
		} finally {
			if (connection != null) {
				if (localThrowable2 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable2.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
	}

	/**
	 * 获得属性
	 */
	public VariableBean get(String key) throws Throwable {
		if (StringHelper.isEmpty(key)) {
			return null;
		}
		VariableBean value = null;
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT F01,F02,F03,F04 FROM _1010 WHERE F01 = ?");
			try {
				pstmt.setString(1, key);
				ResultSet resultSet = pstmt.executeQuery();
				try {
					if (resultSet.next()) {
						value = new VariableBeanImpl(resultSet.getString(1), resultSet.getString(2),
								resultSet.getString(3), resultSet.getString(4), true);
					}
				} catch (Throwable localThrowable1) {
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				throw localThrowable2;
			} finally {
			}
		} catch (Throwable localThrowable3) {
			localThrowable4 = localThrowable3;
			throw localThrowable3;
		} finally {
			if (connection != null) {
				if (localThrowable4 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable4.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
		return value;
	}

	private static class VariableBeanImpl implements VariableBean {
		protected final String key;
		protected final String value;
		protected final String type;
		protected final String desc;
		protected final boolean isInit;

		public VariableBeanImpl(String key, String value, String type, String desc, boolean isInit) {
			this.key = key;
			this.value = value;
			this.type = type;
			this.desc = desc;
			this.isInit = isInit;
		}

		public String getType() {
			return this.type;
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

		public String getDescription() {
			return this.desc;
		}

		public boolean isInit() {
			return this.isInit;
		}
	}

	/**
	 * 同步
	 */
	public void synchronize() throws Throwable {
		VariableType[] variableTypes = this.serviceResource.getVariableTypes();
		if (variableTypes == null) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable5 = null;
		try {
			Set<String> keys = new HashSet();
			PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM _1010");
			try {
				ResultSet resultSet = pstmt.executeQuery();
				try {
					while (resultSet.next()) {
						keys.add(resultSet.getString(1));
					}
				} catch (Throwable localThrowable1) {
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				throw localThrowable2;
			} finally {
			}
			pstmt = connection.prepareStatement("INSERT IGNORE INTO _1010 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?");
			try {
				boolean notEmpty = false;
				for (VariableType variableType : variableTypes) {
					VariableBean[] defalutVariables = variableType.getVariableBeans();
					if ((defalutVariables != null) && (defalutVariables.length != 0)) {
						for (VariableBean defalutVariable : defalutVariables) {
							if ((defalutVariable != null) && (!StringHelper.isEmpty(defalutVariable.getKey()))
									&& (!StringHelper.isEmpty(defalutVariable.getValue()))
									&& (!keys.contains(defalutVariable.getKey())) && (defalutVariable.isInit())) {
								pstmt.setString(1, defalutVariable.getKey());
								pstmt.setString(2, defalutVariable.getValue());
								pstmt.setString(3, defalutVariable.getType());
								pstmt.setString(4, defalutVariable.getDescription());
								pstmt.addBatch();
								notEmpty = true;
							}
						}
					}
				}
				if (notEmpty) {
					pstmt.executeBatch();
				}
			} catch (Throwable localThrowable3) {
				throw localThrowable3;
			} finally {
			}
		} catch (Throwable localThrowable4) {
			localThrowable5 = localThrowable4;
			throw localThrowable4;
		} finally {
			if (connection != null) {
				if (localThrowable5 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable5.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
	}

	/**
	 * 重置
	 */
	public void reset() throws Throwable {
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM _1010 ");
			try {
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				throw localThrowable1;
			} finally {
			}
			VariableType[] variableTypes = this.serviceResource.getVariableTypes();
			if (variableTypes == null) {
				return;
			}
			pstmt = connection.prepareStatement("INSERT IGNORE INTO _1010 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?");
			try {
				boolean notEmpty = false;
				for (VariableType variableType : variableTypes) {
					VariableBean[] defalutVariables = variableType.getVariableBeans();
					if ((defalutVariables != null) && (defalutVariables.length != 0)) {
						for (VariableBean defalutVariable : defalutVariables) {
							if ((defalutVariable != null) && (!StringHelper.isEmpty(defalutVariable.getKey()))
									&& (!StringHelper.isEmpty(defalutVariable.getValue()))) {
								pstmt.setString(1, defalutVariable.getKey());
								pstmt.setString(2, defalutVariable.getValue());
								pstmt.setString(3, defalutVariable.getType());
								pstmt.setString(4, defalutVariable.getDescription());
								pstmt.addBatch();
								notEmpty = true;
							}
						}
					}
				}
				if (notEmpty) {
					pstmt.executeBatch();
				}
			} catch (Throwable localThrowable6) {
				throw localThrowable6;
			} finally {
			}
		} catch (Throwable localThrowable3) {
			localThrowable4 = localThrowable3;
			throw localThrowable3;
		} finally {
			if (connection != null) {
				if (localThrowable4 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable4.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
	}
}
