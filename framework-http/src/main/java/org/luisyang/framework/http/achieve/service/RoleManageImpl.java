package org.luisyang.framework.http.achieve.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.http.entity.RoleBean;
import org.luisyang.framework.http.service.RoleManage;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.framework.service.query.ArrayParser;
import org.luisyang.framework.service.query.ItemParser;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;
import org.luisyang.util.StringHelper;

public class RoleManageImpl extends AbstractHttpService implements RoleManage {
	public static class RoleManageFactory implements ServiceFactory<RoleManage> {
		public RoleManage newInstance(ServiceResource serviceResource) {
			return new RoleManageImpl(serviceResource);
		}
	}

	@SuppressWarnings("unchecked")
	private static ItemParser<RoleBean> ITEM_PARSER = new ItemParser() {
		public RoleBean parse(ResultSet resultSet) throws SQLException {
			RoleManageImpl.RoleBeanImpl role = null;
			if (resultSet.next()) {
				role = new RoleManageImpl.RoleBeanImpl();
				role.roleId = resultSet.getInt(1);
				role.name = resultSet.getString(2);
				role.description = resultSet.getString(3);
				role.createTime = resultSet.getTimestamp(4);
				role.createId = resultSet.getInt(5);
				role.status = resultSet.getString(6);
			}
			return role;
		}
	};
	@SuppressWarnings("unchecked")
	private static ArrayParser<RoleBean> ARRAY_PARSER = new ArrayParser() {
		public RoleBean[] parse(ResultSet resultSet) throws SQLException {
			ArrayList<RoleBean> list = null;
			while (resultSet.next()) {
				if (list == null) {
					list = new ArrayList();
				}
				RoleManageImpl.RoleBeanImpl role = new RoleBeanImpl();
				role.roleId = resultSet.getInt(1);
				role.name = resultSet.getString(2);
				role.description = resultSet.getString(3);
				role.createTime = resultSet.getTimestamp(4);
				role.createId = resultSet.getInt(5);
				role.status = resultSet.getString(6);
				list.add(role);
			}
			return list == null ? new RoleBean[0] : (RoleBean[]) list.toArray(new RoleBean[list.size()]);
		}
	};

	public RoleManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/* Error */
	public int add(String name, String description) throws Throwable {
		return 0;
	}

	public void delete(int roleId) throws Throwable {
		if (roleId <= 0) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable5 = null;
		try {
			this.serviceResource.openTransactions(connection);
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM _1022 WHERE F02 = ? ");
			Throwable localThrowable6 = null;
			try {
				pstmt.setInt(1, roleId);
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				localThrowable6 = localThrowable1;
				throw localThrowable1;
			} finally {
			}
			pstmt = connection.prepareStatement("DELETE FROM _1021 WHERE F01 = ? ");
			localThrowable6 = null;
			try {
				pstmt.setInt(1, roleId);
				pstmt.execute();
			} catch (Throwable localThrowable2) {
				localThrowable6 = localThrowable2;
				throw localThrowable2;
			} finally {
			}
			pstmt = connection.prepareStatement("DELETE FROM _1020 WHERE F01 = ? ");
			localThrowable6 = null;
			try {
				pstmt.setInt(1, roleId);
				pstmt.execute();
			} catch (Throwable localThrowable3) {
				localThrowable6 = localThrowable3;
				throw localThrowable3;
			} finally {
			}
			this.serviceResource.commit(connection);
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

	public void active(int roleId) throws Throwable {
		if (roleId <= 0) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable3 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE _1020 SET F06 = ? WHERE F01 = ? ");
			Throwable localThrowable4 = null;
			try {
				pstmt.setString(1, "QY");
				pstmt.setInt(2, roleId);
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				localThrowable4 = localThrowable1;
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
	}

	public void inActive(int roleId) throws Throwable {
		if (roleId <= 0) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable3 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE _1020 SET F06 = ? WHERE F01 = ? ");
			Throwable localThrowable4 = null;
			try {
				pstmt.setString(1, "TY");
				pstmt.setInt(2, roleId);
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				localThrowable4 = localThrowable1;
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
	}

	public PagingResult<RoleBean> search(RoleManage.RoleQuery condition, Paging paging) throws Throwable {
		StringBuilder builder = new StringBuilder("SELECT F01, F02, F03, F04, F05, F06 FROM _1020 WHERE 1 = 1 ");

		ArrayList<Object> parameters = new ArrayList();
		SQLConnectionProvider connectionProvider = getConnectionProvider();
		if (condition != null) {
			if (condition.getRoleId() > 0) {
				builder.append(" AND F01 = ?");
				parameters.add(Integer.valueOf(condition.getRoleId()));
			}
			if (!StringHelper.isEmpty(condition.getName())) {
				builder.append(" AND F02 LIKE ?");
				parameters.add(connectionProvider.allMatch(condition.getName()));
			}
			if (!StringHelper.isEmpty(condition.getName())) {
				builder.append(" AND F03 LIKE ?");
				parameters.add(connectionProvider.allMatch(condition.getDescription()));
			}
			if (condition.getCreateTimeStart() != null) {
				builder.append(" AND F04 >= ?");
				parameters.add(condition.getCreateTimeStart());
			}
			if (condition.getCreateTimeEnd() != null) {
				builder.append(" AND F04 <= ?");
				parameters.add(condition.getCreateTimeEnd());
			}
			if (condition.getCreaterId() > 0) {
				builder.append(" AND F05 = ?");
				parameters.add(Integer.valueOf(condition.getCreaterId()));
			}
			if (!StringHelper.isEmpty(condition.getStatus())) {
				builder.append(" AND F06 = ?");
				parameters.add(condition.getStatus());
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

	public RoleBean[] getRoles(int userId) throws Throwable {
		if (userId <= 0) {
			return new RoleBean[0];
		}
		Connection connection = getConnection();
		Throwable localThrowable2 = null;
		try {
			return (RoleBean[]) selectAll(connection, ARRAY_PARSER,
					"SELECT _1020.F01, _1020.F02, _1020.F03, _1020.F04, _1020.F05, _1020.F06 FROM _1022 INNER JOIN _1020 ON _1022.F02 = _1020.F01 WHERE _1022.F01 = ?",
					new Object[] { Integer.valueOf(userId) });
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

	public RoleBean getRole(int roleId) throws Throwable {
		if (roleId <= 0) {
			return null;
		}
		Connection connection = getConnection();
		Throwable localThrowable2 = null;
		try {
			return (RoleBean) select(connection, ITEM_PARSER,
					"SELECT F01, F02, F03, F04, F05, F06 FROM _1020 WHERE F01 = ?",
					new Object[] { Integer.valueOf(roleId) });
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

	public void setRoles(int userId, int... roleIds) throws Throwable {
		if (userId <= 0) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			this.serviceResource.openTransactions(connection);
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM _1022 WHERE F01 = ?");
			Throwable localThrowable5 = null;
			try {
				pstmt.setInt(1, userId);
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				localThrowable5 = localThrowable1;
				throw localThrowable1;
			} finally {
			}
			if (roleIds != null) {
				pstmt = connection.prepareStatement("INSERT INTO _1022 SET F01 = ?, F02 = ?");
				localThrowable5 = null;
				try {
					for (int roleId : roleIds) {
						if (roleId > 0) {
							pstmt.setInt(1, userId);
							pstmt.setInt(2, roleId);
							pstmt.addBatch();
						}
					}
					pstmt.executeBatch();
				} catch (Throwable localThrowable2) {
					localThrowable5 = localThrowable2;
					throw localThrowable2;
				} finally {
				}
			}
			this.serviceResource.commit(connection);
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

	private static class RoleBeanImpl implements RoleBean {
		private static final long serialVersionUID = 1L;
		int roleId;
		String name;
		String description;
		Timestamp createTime;
		int createId;
		String status;

		public int getRoleId() {
			return this.roleId;
		}

		public String getName() {
			return this.name;
		}

		public String getDescription() {
			return this.description;
		}

		public Timestamp getCreateTime() {
			return this.createTime;
		}

		public int getCreaterId() {
			return this.createId;
		}

		public String getStatus() {
			return this.status;
		}
	}
}