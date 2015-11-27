package org.luisyang.framework.message.achieve.email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.luisyang.framework.message.email.EmailSender;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.util.StringHelper;

public class EmailSenderImpl extends AbstractEmailService implements EmailSender {
	public EmailSenderImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class EmailSenderFactory implements ServiceFactory<EmailSender> {
		public EmailSender newInstance(ServiceResource serviceResource) {
			return new EmailSenderImpl(serviceResource);
		}
	}

	public void send(int type, String subject, String content, String... addresses) throws Throwable {
		if ((StringHelper.isEmpty(subject)) || (StringHelper.isEmpty(content)) || (addresses == null)
				|| (addresses.length <= 0)) {
			return;
		}
		int i = 0;
		List<String> emails = new ArrayList();
		int userId = 0;
		try {
			userId = this.serviceResource.getSession().getAccountId();
		} catch (Exception e) {
			userId = 0;
		}
		for (String address : addresses) {
			i++;
			if ((i % 100 != 0) && (i != addresses.length)) {
				emails.add(address);
			} else {
				emails.add(address);
				if ((emails != null) && (emails.size() > 0)) {
					Connection connection = getConnection();
					Throwable localThrowable5 = null;
					try {
						long msgId = 0L;
						PreparedStatement ps = connection
								.prepareStatement("INSERT INTO _1046(F02,F03,F04,F05,F07,F08) VALUES(?,?,?,?,?,?)", 1);
						Throwable localThrowable6 = null;
						try {
							ps.setString(1, subject);
							ps.setString(2, content);
							ps.setInt(3, type);
							ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
							ps.setString(5, "W");
							ps.setInt(6, userId);

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
									.prepareStatement("INSERT INTO _1047(F01,F02) VALUES(?,?)");
							localThrowable6 = null;
							try {
								for (String email : emails) {
									ps.setLong(1, msgId);
									ps.setString(2, email);
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
					emails.clear();
				}
			}
		}
	}
}
