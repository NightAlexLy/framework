package org.luisyang.framework.message.achieve.sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.luisyang.framework.message.sms.SmsSender;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.util.StringHelper;

public class SmsSenderImpl extends AbstractSmsService implements SmsSender {
	public SmsSenderImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public void send(int type, String message, String... receivers) throws Throwable {
		if ((StringHelper.isEmpty(message)) || (receivers == null) || (receivers.length <= 0)) {
			return;
		}
		int i = 0;
		List<String> phones = new ArrayList();
		int userId = 0;
		try {
			userId = this.serviceResource.getSession().getAccountId();
		} catch (Exception e) {
			userId = 0;
		}
		for (String receiver : receivers) {
			i++;
			if ((i % 100 != 0) && (i != receivers.length)) {
				phones.add(receiver);
			} else {
				phones.add(receiver);
				if ((phones != null) && (phones.size() > 0)) {
					Connection connection = getConnection();
					Throwable localThrowable5 = null;
					try {
						long msgId = 0L;
						PreparedStatement ps = connection
								.prepareStatement("INSERT INTO _1040(F02,F03,F04,F05,F07) values(?,?,?,?,?)", 1);
						Throwable localThrowable6 = null;
						try {
							ps.setInt(1, type);
							ps.setString(2, message);
							ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
							ps.setString(4, "W");
							ps.setInt(5, userId);
							ps.execute();
							ResultSet resultSet = ps.getGeneratedKeys();
							Throwable localThrowable7 = null;
							try {
								if (resultSet.next()) {
									msgId = resultSet.getLong(1);
								}
							} catch (Throwable localThrowable1) {
								localThrowable7 = localThrowable1;
								throw localThrowable1;
							} finally {
							}
						} catch (Throwable localThrowable2) {
							localThrowable6 = localThrowable2;
							throw localThrowable2;
						} finally {
						}
						if (msgId > 0L) {
							ps = connection
									.prepareStatement("INSERT INTO _1041(F01,F02) VALUES(?,?)");
							localThrowable6 = null;
							try {
								for (String phone : phones) {
									ps.setLong(1, msgId);
									ps.setString(2, phone);
									ps.addBatch();
								}
								ps.executeBatch();
							} catch (Throwable localThrowable3) {
								localThrowable6 = localThrowable3;
								throw localThrowable3;
							} finally {
							}
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
					phones.clear();
				}
			}
		}
	}

	public static class SenderFactory implements ServiceFactory<SmsSender> {
		public SmsSender newInstance(ServiceResource serviceResource) {
			return new SmsSenderImpl(serviceResource);
		}
	}
}
