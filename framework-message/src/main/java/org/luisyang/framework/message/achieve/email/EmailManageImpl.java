package org.luisyang.framework.message.achieve.email;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.message.email.EmailManage;
import org.luisyang.framework.message.email.entity.EmailTask;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.framework.service.query.ArrayParser;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;
import org.luisyang.util.StringHelper;

public class EmailManageImpl extends AbstractEmailService implements EmailManage {
	public EmailManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class EmailSendFactory implements ServiceFactory<EmailManage> {
		public EmailManage newInstance(ServiceResource serviceResource) {
			return new EmailManageImpl(serviceResource);
		}
	}

	@SuppressWarnings("unchecked")
	private static final ArrayParser<EmailTask> ARRAY_PARSER = new ArrayParser() {
		public EmailTask[] parse(ResultSet resultSet) throws SQLException {
			List<EmailTask> emailTasks = null;
			while (resultSet.next()) {
				if (emailTasks == null) {
					emailTasks = new ArrayList();
				}
				EmailTask emailTask = new EmailTask();
				emailTask.id = resultSet.getLong(1);
				emailTask.subject = resultSet.getString(2);
				emailTask.content = resultSet.getString(3);
				emailTask.type = resultSet.getInt(4);
				emailTasks.add(emailTask);
			}
			return emailTasks == null ? null : (EmailTask[]) emailTasks.toArray(new EmailTask[emailTasks.size()]);
		}
	};

	public PagingResult<EmailTask> searchUnsendTask(EmailManage.EmailTaskQuery emailTaskQuery, Paging paging)
			throws Throwable {
		StringBuilder builder = new StringBuilder("SELECT F01,F02,F03,F04 FROM _1046 WHERE 1=1");

		List<Object> parameters = new ArrayList();
		SQLConnectionProvider connectionProvider = getConnectionProvider();
		if (emailTaskQuery != null) {
			if (emailTaskQuery.getId() > 0) {
				builder.append(" AND F01=?");
				parameters.add(Integer.valueOf(emailTaskQuery.getId()));
			}
			if (!StringHelper.isEmpty(emailTaskQuery.getSubject())) {
				builder.append(" AND F02=?");
				parameters.add(connectionProvider.allMatch(emailTaskQuery.getSubject()));
			}
			if (!StringHelper.isEmpty(emailTaskQuery.getContent())) {
				builder.append(" AND F03=?");
				parameters.add(connectionProvider.allMatch(emailTaskQuery.getContent()));
			}
			if (emailTaskQuery.getType() > 0) {
				builder.append(" AND F04=?");
				parameters.add(Integer.valueOf(emailTaskQuery.getType()));
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

	public PagingResult<EmailTask> searchSendTask(EmailManage.EmailTaskQuery emailTaskQuery, Paging paging)
			throws Throwable {
		StringBuilder builder = new StringBuilder("SELECT F01,F02,F03,F04 FROM _1048 WHERE 1=1");

		List<Object> parameters = new ArrayList();
		SQLConnectionProvider connectionProvider = getConnectionProvider();
		if (emailTaskQuery != null) {
			if (emailTaskQuery.getId() > 0) {
				builder.append(" AND F01=?");
				parameters.add(Integer.valueOf(emailTaskQuery.getId()));
			}
			if (!StringHelper.isEmpty(emailTaskQuery.getSubject())) {
				builder.append(" AND F02=?");
				parameters.add(connectionProvider.allMatch(emailTaskQuery.getSubject()));
			}
			if (!StringHelper.isEmpty(emailTaskQuery.getContent())) {
				builder.append(" AND F03=?");
				parameters.add(connectionProvider.allMatch(emailTaskQuery.getContent()));
			}
			if (emailTaskQuery.getType() > 0) {
				builder.append(" AND F04=?");
				parameters.add(Integer.valueOf(emailTaskQuery.getType()));
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
}
