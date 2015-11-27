package org.luisyang.framework.message.achieve.sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.luisyang.framework.message.sms.Extracter;
import org.luisyang.framework.message.sms.entity.SmsTask;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;

public class SmsExtracterImpl extends AbstractSmsService implements Extracter {
	public SmsExtracterImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public SmsTask[] extract(int maxCount, int expiresMinutes) throws Throwable {
		if (maxCount <= 0) {
			return null;
		}
		if (expiresMinutes <= 0) {
			expiresMinutes = 15;
		}
		List<SmsTask> sendTasks = null;
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT M.F01,M.F02,M.F03,GROUP_CONCAT(N.F02) FROM _1040 M,_1041 N WHERE M.F01 = N.F01 AND M.F05= ? GROUP BY M.F01 ORDER BY M.F04 ASC LIMIT 0,?");
			Throwable localThrowable5 = null;
			try {
				ps.setString(1, "W");
				ps.setInt(2, maxCount);
				ResultSet resultSet = ps.executeQuery();
				Throwable localThrowable6 = null;
				try {
					while (resultSet.next()) {
						if (sendTasks == null) {
							sendTasks = new ArrayList();
						}
						SmsTask sendTask = new SmsTask();
						sendTask.id = resultSet.getLong(1);
						sendTask.type = resultSet.getInt(2);
						sendTask.content = resultSet.getString(3);
						String recs = resultSet.getString(4);
						if ((null != recs) && (!"".equals(recs.trim()))) {
							sendTask.receivers = recs.split(",");

							sendTasks.add(sendTask);
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
			if (sendTasks == null) {
				return null;
			}
			try {
				this.serviceResource.openTransactions(connection);
				ps = connection.prepareStatement("UPDATE _1040 SET F05=?,F06=? WHERE F01=?");
				long l = System.currentTimeMillis();
				for (SmsTask sendTask : sendTasks) {
					if (sendTask != null) {
						ps.setString(1, "Z");
						ps.setTimestamp(2, new Timestamp(l + expiresMinutes * 60 * 1000));
						ps.setLong(3, sendTask.id);
						ps.addBatch();
					}
				}
				ps.executeBatch();
				this.serviceResource.commit(connection);
			} catch (Throwable e) {
				this.serviceResource.error("SmsExtracterImpl.extract", e);
				this.serviceResource.rollback(connection);
				throw new Exception("UPDATE _1040 SET F05=?,F06=? WHERE F01=?   is error");
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
		return sendTasks == null ? null : (SmsTask[]) sendTasks.toArray(new SmsTask[sendTasks.size()]);
	}

	public void mark(long id, boolean success, String extra) throws Throwable {
		if (id <= 0L) {
			return;
		}
		Connection connection = getConnection();
		try {
			this.serviceResource.openTransactions(connection);
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO _1042(F01,F02,F03,F04,F05,F06) SELECT F01,F02,F03,F04,?,? FROM _1040 WHERE F01=?");
			if (success) {
				ps.setString(1, "YES");
			} else {
				ps.setString(1, "NO");
			}
			ps.setString(2, extra);
			ps.setLong(3, id);
			ps.execute();

			PreparedStatement ps2 = connection.prepareStatement("UPDATE _1040 set F05 = 'Y' WHERE F01=?");
			ps2.setLong(1, id);
			ps2.execute();

			this.serviceResource.commit(connection);
		} catch (Throwable e) {
			this.serviceResource.error("SmsExtracterImpl.mark", e);
			this.serviceResource.rollback(connection);
			throw new Exception("SmsExtracterImpl.mark Exception");
		}
	}

	public static class SmsExtracterFactory implements ServiceFactory<Extracter> {
		public Extracter newInstance(ServiceResource serviceResource) {
			return new SmsExtracterImpl(serviceResource);
		}
	}
}