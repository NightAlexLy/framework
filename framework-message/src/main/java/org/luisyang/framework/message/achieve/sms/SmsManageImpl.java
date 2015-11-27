package org.luisyang.framework.message.achieve.sms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.message.sms.SmsManage;
import org.luisyang.framework.message.sms.entity.SmsTask;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.framework.service.query.ArrayParser;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;
import org.luisyang.util.StringHelper;

public class SmsManageImpl extends AbstractSmsService implements SmsManage {
	public SmsManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final ArrayParser<SmsTask> ARRAY_PARSER = new ArrayParser() {
		public SmsTask[] parse(ResultSet resultSet) throws SQLException {
			List<SmsTask> sendTasks = null;
			while (resultSet.next()) {
				if (sendTasks == null) {
					sendTasks = new ArrayList();
				}
				SmsTask sendTask = new SmsTask();
				sendTask.id = resultSet.getLong(1);
				sendTask.type = resultSet.getInt(2);
				sendTask.content = resultSet.getString(3);
				sendTasks.add(sendTask);
			}
			return sendTasks == null ? null : (SmsTask[]) sendTasks.toArray(new SmsTask[sendTasks.size()]);
		}
	};

	public PagingResult<SmsTask> searchUnsendTask(SmsManage.SendTaskQuery sendTaskQuery, Paging paging)
			throws Throwable {
		StringBuilder builder = new StringBuilder("SELECT F01,F02,F03 FROM _1040 WHERE 1=1");

		SQLConnectionProvider connectionProvider = getConnectionProvider();
		ArrayList<Object> parameters = new ArrayList();
		if (sendTaskQuery != null) {
			if (sendTaskQuery.getId() > 0) {
				builder.append(" AND F01=?");
				parameters.add(Integer.valueOf(sendTaskQuery.getId()));
			}
			if (sendTaskQuery.getType() > 0) {
				builder.append(" AND F02=?");
				parameters.add(Integer.valueOf(sendTaskQuery.getType()));
			}
			if (!StringHelper.isEmpty(sendTaskQuery.getMessage())) {
				builder.append(" AND F03=?");
				parameters.add(connectionProvider.allMatch(sendTaskQuery.getMessage()));
			}
			if (StringHelper.isEmpty(sendTaskQuery.getStatus())) {
				builder.append(" AND F05=?");
				parameters.add(sendTaskQuery.getStatus());
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

	public PagingResult<SmsTask> searchSendTask(SmsManage.SendTaskQuery sendTaskQuery, Paging paging) throws Throwable {
		StringBuilder builder = new StringBuilder("SELECT F01,F02,F03 FROM _1042 WHERE 1=1");

		SQLConnectionProvider connectionProvider = getConnectionProvider();
		ArrayList<Object> parameters = new ArrayList();
		if (sendTaskQuery != null) {
			if (sendTaskQuery.getId() > 0) {
				builder.append(" AND F01=?");
				parameters.add(Integer.valueOf(sendTaskQuery.getId()));
			}
			if (sendTaskQuery.getType() > 0) {
				builder.append(" AND F02=?");
				parameters.add(Integer.valueOf(sendTaskQuery.getType()));
			}
			if (!StringHelper.isEmpty(sendTaskQuery.getMessage())) {
				builder.append(" AND F03=?");
				parameters.add(connectionProvider.allMatch(sendTaskQuery.getMessage()));
			}
			if (StringHelper.isEmpty(sendTaskQuery.getStatus())) {
				builder.append(" AND F05=?");
				parameters.add(sendTaskQuery.getStatus());
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

	public static class SmsSendFactory implements ServiceFactory<SmsManage> {
		public SmsManage newInstance(ServiceResource serviceResource) {
			return new SmsManageImpl(serviceResource);
		}
	}
}