package org.luisyang.framework.message.achieve.email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.luisyang.framework.message.email.Extracter;
import org.luisyang.framework.message.email.entity.EmailTask;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;

public class EmailExtracterImpl extends AbstractEmailService implements Extracter {
	public EmailExtracterImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public EmailTask[] extract(int maxCount, int expiresMinutes) throws Throwable {
		if (maxCount <= 0) {
			return null;
		}
		if (expiresMinutes <= 0) {
			expiresMinutes = 30;
		}
		List<EmailTask> emailTasks = null;
		Connection connection = getConnection();
		Throwable localThrowable4 = null;
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT M.F01,M.F02,M.F03,M.F05,GROUP_CONCAT(N.F02) FROM S10._1046 M,S10._1047 N WHERE M.F01 = N.F01 AND M.F07= ? GROUP BY M.F01 ORDER BY M.F06 ASC LIMIT 0,?");
			Throwable localThrowable5 = null;
			try {
				ps.setString(1, "W");
				ps.setInt(2, maxCount);
				ResultSet resultSet = ps.executeQuery();
				Throwable localThrowable6 = null;
				try {
					while (resultSet.next()) {
						if (emailTasks == null) {
							emailTasks = new ArrayList();
						}
						EmailTask emailTask = new EmailTask();
						emailTask.id = resultSet.getLong(1);
						emailTask.subject = resultSet.getString(2);
						emailTask.content = resultSet.getString(3);
						emailTask.type = resultSet.getInt(4);
						String recs = resultSet.getString(5);
						if ((null != recs) && (!"".equals(recs.trim()))) {
							emailTask.addresses = recs.split(",");

							emailTasks.add(emailTask);
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
			if (emailTasks == null) {
				return null;
			}
			try {
				this.serviceResource.openTransactions(connection);
				ps = connection.prepareStatement("UPDATE _1046 SET F06=?,F07=? WHERE F01=?");
				long l = System.currentTimeMillis();
				for (EmailTask emailTask : emailTasks) {
					ps.setTimestamp(1, new Timestamp(l + expiresMinutes * 60 * 1000));

					ps.setString(2, "Z");
					ps.setLong(3, emailTask.id);
					ps.addBatch();
				}
				ps.executeBatch();
				this.serviceResource.commit(connection);
			} catch (Throwable e) {
				this.serviceResource.error("EmailExtracterImpl.extract", e);
				this.serviceResource.rollback(connection);
				throw new Exception("UPDATE _1046 SET F06=?,F07=? WHERE F01=?  is error");
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
		return emailTasks == null ? null : (EmailTask[]) emailTasks.toArray(new EmailTask[emailTasks.size()]);
	}

	public void mark(long id) throws Throwable {
		if (id <= 0L) {
			return;
		}
		Connection connection = getConnection();
		try {
			this.serviceResource.openTransactions(connection);
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO _1048(F01,F02,F03,F04,F05) SELECT F01,F02,F03,F04,F05 FROM _1046 WHERE F01=?");

			ps.setLong(1, id);
			ps.addBatch();
			ps.execute();

			PreparedStatement ps2 = connection.prepareStatement("UPDATE _1046 SET F07='Y' WHERE F01=?");
			ps2.setLong(1, id);
			ps2.execute();
			this.serviceResource.commit(connection);
		} catch (Throwable e) {
			this.serviceResource.error("EmailExtracterImpl.mark", e);
			this.serviceResource.rollback(connection);
			throw new Exception("EmailExtracterImpl.mark  is error");
		}
	}

	public static class EmailExtracterFactory implements ServiceFactory<Extracter> {
		public Extracter newInstance(ServiceResource serviceResource) {
			return new EmailExtracterImpl(serviceResource);
		}
	}
}