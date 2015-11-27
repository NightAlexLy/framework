package org.luisyang.framework.http.achieve.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.luisyang.framework.http.entity.RightBean;
import org.luisyang.framework.http.service.RightManage;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.util.StringHelper;

public class RightManageImpl extends AbstractHttpService implements RightManage {
	public static class RightManageFactory implements ServiceFactory<RightManage> {
		public RightManage newInstance(ServiceResource serviceResource) {
			return new RightManageImpl(serviceResource);
		}
	}

	public RightManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public boolean hasRight(int userId, String rightId) throws Throwable {
		if ((userId <= 0) || (StringHelper.isEmpty(rightId))) {
			return false;
		}
		Connection connection = getConnection();
		Throwable localThrowable6 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT 1 FROM _1023 WHERE F01 = ? AND F02 = ?");
			Throwable localThrowable7 = null;
			Throwable localThrowable8;
			try {
				pstmt.setInt(1, userId);
				pstmt.setString(2, rightId);
				ResultSet resultSet = pstmt.executeQuery();
				localThrowable8 = null;
				try {
					if (resultSet.next()) {
						return true;
					}
				} catch (Throwable localThrowable9) {
					localThrowable8 = localThrowable9;
					throw localThrowable9;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				localThrowable7 = localThrowable2;
				throw localThrowable2;
			} finally {
			}
			pstmt = connection.prepareStatement(
					"SELECT 1 FROM _1022 STRAIGHT_JOIN _1020 ON _1022.F02 = _1020.F01 AND _1020.F06 = 'QY' STRAIGHT_JOIN _1021  ON _1022.F02 = _1021.F01 WHERE _1022.F01 = ? AND _1021.F02 = ? LIMIT 1");
			localThrowable7 = null;
			try {
				pstmt.setInt(1, userId);
				pstmt.setString(2, rightId);
				ResultSet resultSet = pstmt.executeQuery();
				localThrowable8 = null;
				try {
					if (resultSet.next()) {
						return true;
					}
				} catch (Throwable localThrowable10) {
					localThrowable8 = localThrowable10;
					throw localThrowable10;
				} finally {
				}
			} catch (Throwable localThrowable4) {
				localThrowable7 = localThrowable4;
				throw localThrowable4;
			} finally {
			}
		} catch (Throwable localThrowable5) {
			localThrowable6 = localThrowable5;
			throw localThrowable5;
		} finally {
			if (connection != null) {
				if (localThrowable6 != null) {
					try {
						connection.close();
					} catch (Throwable x2) {
						localThrowable6.addSuppressed(x2);
					}
				} else {
					connection.close();
				}
			}
		}
		return false;
	}

	public void setUserRights(int userId, String... rightIds) throws Throwable {
		if (userId <= 0) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			this.serviceResource.openTransactions(connection);
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM _1023 WHERE F01 = ?");
			Throwable localThrowable5 = null;
			try {
				pstmt.setInt(1, userId);
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				localThrowable5 = localThrowable1;
				throw localThrowable1;
			} finally {
			}
			if (rightIds != null) {
				pstmt = connection.prepareStatement("INSERT INTO _1023 SET F01 = ?, F02 = ?");
				localThrowable5 = null;
				try {
					for (String rightId : rightIds) {
						if (!StringHelper.isEmpty(rightId)) {
							pstmt.setInt(1, userId);
							pstmt.setString(2, rightId);
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

	public void setRoleRights(int roleId, String... rightIds) throws Throwable {
		if (roleId <= 0) {
			return;
		}
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			this.serviceResource.openTransactions(connection);
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM _1021 WHERE F01 = ?");
			Throwable localThrowable5 = null;
			try {
				pstmt.setInt(1, roleId);
				pstmt.execute();
			} catch (Throwable localThrowable1) {
				localThrowable5 = localThrowable1;
				throw localThrowable1;
			} finally {
			}
			if (rightIds != null) {
				pstmt = connection.prepareStatement("INSERT INTO _1021 SET F01 = ?, F02 = ?");
				localThrowable5 = null;
				try {
					for (String rightId : rightIds) {
						if (!StringHelper.isEmpty(rightId)) {
							pstmt.setInt(1, roleId);
							pstmt.setString(2, rightId);
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

	public RightBean[] getUserRights(int userId) throws Throwable {
		if (userId <= 0) {
			return new RightBean[0];
		}
		ArrayList<RightBean> rights = new ArrayList();
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1023 WHERE F01 = ?");
			Throwable localThrowable5 = null;
			try {
				pstmt.setInt(1, userId);
				ResultSet resultSet = pstmt.executeQuery();
				Throwable localThrowable6 = null;
				try {
					while (resultSet.next()) {
						RightBean rightBean = this.serviceResource.getRightBean(resultSet.getString(1));
						if (rightBean != null) {
							rights.add(rightBean);
						}
					}
				} catch (Throwable localThrowable1) {
					localThrowable6 = localThrowable1;
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				localThrowable5 = localThrowable2;
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
		return (RightBean[]) rights.toArray(new RightBean[rights.size()]);
	}

	public RightBean[] getRoleRights(int roleId) throws Throwable {
		if (roleId <= 0) {
			return new RightBean[0];
		}
		ArrayList<RightBean> rights = new ArrayList();
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1021 WHERE F01 = ?");
			Throwable localThrowable5 = null;
			try {
				pstmt.setInt(1, roleId);
				ResultSet resultSet = pstmt.executeQuery();
				Throwable localThrowable6 = null;
				try {
					while (resultSet.next()) {
						RightBean rightBean = this.serviceResource.getRightBean(resultSet.getString(1));
						if (rightBean != null) {
							rights.add(rightBean);
						}
					}
				} catch (Throwable localThrowable1) {
					localThrowable6 = localThrowable1;
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				localThrowable5 = localThrowable2;
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
		return (RightBean[]) rights.toArray(new RightBean[rights.size()]);
	}
}