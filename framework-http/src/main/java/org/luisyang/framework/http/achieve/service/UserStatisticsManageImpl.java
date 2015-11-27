package org.luisyang.framework.http.achieve.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.luisyang.framework.http.service.UserStatisticsManage;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;

public class UserStatisticsManageImpl extends AbstractHttpService implements UserStatisticsManage {
	public static class RightManageFactory implements ServiceFactory<UserStatisticsManage> {
		public UserStatisticsManage newInstance(ServiceResource serviceResource) {
			return new UserStatisticsManageImpl(serviceResource);
		}
	}

	public UserStatisticsManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public int getLoginCount(Date date) throws Throwable {
		Connection connection = getConnection();
		Throwable localThrowable6 = null;
		try {
			Throwable localThrowable7;
			Throwable localThrowable8;
			if (date == null) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM _1034");
				localThrowable7 = null;
				try {
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						if (resultSet.next()) {
							return resultSet.getInt(1);
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
			} else {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1035 WHERE F01 = ?");
				localThrowable7 = null;
				try {
					pstmt.setDate(1, date);
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						if (resultSet.next()) {
							return resultSet.getInt(1);
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
		return 0;
	}

	public int getOnlineCount() throws Throwable {
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT COUNT(*) FROM _1030 WHERE F04 IS NOT NULL AND F05 > CURRENT_TIMESTAMP()");
			Throwable localThrowable5 = null;
			try {
				ResultSet resultSet = pstmt.executeQuery();
				Throwable localThrowable6 = null;
				try {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				} catch (Throwable localThrowable7) {
					localThrowable6 = localThrowable7;
					throw localThrowable7;
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
		return 0;
	}

	public int[] getOnlineHistory(Date date) throws Throwable {
		int[] values = new int[24];
		Connection connection = getConnection();
		Throwable localThrowable6 = null;
		try {
			Throwable localThrowable7;
			Throwable localThrowable8;
			if (date == null) {
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT F02,F03 FROM _1036 WHERE F01 = CURRENT_DATE()");
				localThrowable7 = null;
				try {
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						if (resultSet.next()) {
							values[(resultSet.getInt(1) % 24)] = resultSet.getInt(2);
						}
					} catch (Throwable localThrowable1) {
						localThrowable8 = localThrowable1;
						throw localThrowable1;
					} finally {
					}
				} catch (Throwable localThrowable2) {
					localThrowable7 = localThrowable2;
					throw localThrowable2;
				} finally {
				}
			} else {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F02,F03 FROM _1036 WHERE F01 = ?");
				localThrowable7 = null;
				try {
					pstmt.setDate(1, date);
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						if (resultSet.next()) {
							values[(resultSet.getInt(1) % 24)] = resultSet.getInt(2);
						}
					} catch (Throwable localThrowable3) {
						localThrowable8 = localThrowable3;
						throw localThrowable3;
					} finally {
					}
				} catch (Throwable localThrowable4) {
					localThrowable7 = localThrowable4;
					throw localThrowable4;
				} finally {
				}
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
		return values;
	}

	public int getLoginCount(Date date, String schemaName) throws Throwable {
		Connection connection = getConnection(schemaName);
		Throwable localThrowable6 = null;
		try {
			Throwable localThrowable7;
			Throwable localThrowable8;
			if (date == null) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM _1034");
				localThrowable7 = null;
				try {
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						if (resultSet.next()) {
							return resultSet.getInt(1);
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
			} else {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1035 WHERE F01 = ?");
				localThrowable7 = null;
				try {
					pstmt.setDate(1, date);
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						if (resultSet.next()) {
							return resultSet.getInt(1);
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
		return 0;
	}

	public int getOnlineCount(String schemaName) throws Throwable {
		Connection connection = getConnection(schemaName);
		Throwable localThrowable4 = null;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT COUNT(*) FROM _1030 WHERE F04 IS NOT NULL AND F05 > CURRENT_TIMESTAMP()");
			Throwable localThrowable5 = null;
			try {
				ResultSet resultSet = pstmt.executeQuery();
				Throwable localThrowable6 = null;
				try {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				} catch (Throwable localThrowable7) {
					localThrowable6 = localThrowable7;
					throw localThrowable7;
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
		return 0;
	}

	public int[] getOnlineHistory(Date date, String schemaName) throws Throwable {
		int[] values = new int[24];
		Connection connection = getConnection(schemaName);
		Throwable localThrowable6 = null;
		try {
			Throwable localThrowable7;
			Throwable localThrowable8;
			if (date == null) {
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT F02,F03 FROM _1036 WHERE F01 = CURRENT_DATE()");
				localThrowable7 = null;
				try {
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						while (resultSet.next()) {
							values[(resultSet.getInt(1) % 24)] = resultSet.getInt(2);
						}
					} catch (Throwable localThrowable1) {
						localThrowable8 = localThrowable1;
						throw localThrowable1;
					} finally {
					}
				} catch (Throwable localThrowable2) {
					localThrowable7 = localThrowable2;
					throw localThrowable2;
				} finally {
				}
			} else {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F02,F03 FROM _1036 WHERE F01 = ?");
				localThrowable7 = null;
				try {
					pstmt.setDate(1, date);
					ResultSet resultSet = pstmt.executeQuery();
					localThrowable8 = null;
					try {
						while (resultSet.next()) {
							values[(resultSet.getInt(1) % 24)] = resultSet.getInt(2);
						}
					} catch (Throwable localThrowable3) {
						localThrowable8 = localThrowable3;
						throw localThrowable3;
					} finally {
					}
				} catch (Throwable localThrowable4) {
					localThrowable7 = localThrowable4;
					throw localThrowable4;
				} finally {
				}
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
		return values;
	}
}