package org.luisyang.framework.http.achieve.session;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.http.service.RightManage;
import org.luisyang.framework.http.servlet.annotation.Right;
import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.http.session.SessionManager;
import org.luisyang.framework.http.session.VerifyCode;
import org.luisyang.framework.http.session.VerifyCodeGenerator;
import org.luisyang.framework.http.session.authentication.AccesssDeniedException;
import org.luisyang.framework.http.session.authentication.AuthenticationException;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.service.ServiceProvider;
import org.luisyang.framework.service.ServiceSession;
import org.luisyang.util.parser.LongParser;

public abstract class AbstractSessionManager extends SessionManager {
	protected final VerifyCodeGenerator DEFAULT_VERIFY_CODE_GENERATOR = new VerifyCodeGenerator() {
		private final SecureRandom random = new SecureRandom();

		public VerifyCode newVerifyCode() {
			int length = AbstractSessionManager.this.resourceProvider.getSystemDefine().getVerifyCodeLength();
			char[] value = new char[length];
			for (int i = 0; i < length; i++) {
				value[i] = ((char) (this.random.nextInt(10) + 48));
			}
			final String displayValue = new String(value);

			return new VerifyCode() {
				public String getMatchValue() {
					return displayValue;
				}

				public String getDisplayValue() {
					return displayValue;
				}

				public String toString() {
					return displayValue;
				}
			};
		}

		public long getTTL() {
			long ttl = LongParser.parse(((ConfigureProvider) AbstractSessionManager.this.resourceProvider
					.getResource(ConfigureProvider.class)).getProperty("SYSTEM.VERIFY_CODE_TIME_OUT"));
			if (0L == ttl) {
				return 180000L;
			}
			return ttl;
		}
	};

	public AbstractSessionManager(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	public Session getSession(HttpServletRequest request, HttpServletResponse response) {
		return getSession(request, response, true);
	}

	protected abstract class AbstractSession implements Session {
		protected int accountId;

		protected AbstractSession() {
		}

		protected abstract void checkMaxErrorTimes(String paramString) throws AuthenticationException;

		protected abstract void markError(String paramString);

		public String getVerifyCode(String type) {
			return getVerifyCode(type, AbstractSessionManager.this.DEFAULT_VERIFY_CODE_GENERATOR);
		}

		public void tryAccessResource(String resourceId) throws AccesssDeniedException {
			if (isAccessableResource(resourceId)) {
				return;
			}
			throw new AccesssDeniedException(String.format("拒绝访问:%s.", new Object[] { resourceId }));
		}

		public boolean isAccessableResource(Class<?> resource) throws AccesssDeniedException {
			if (resource == null) {
				return true;
			}
			Right right = (Right) resource.getAnnotation(Right.class);
			if (right == null) {
				return true;
			}
			return isAccessableResource(right.id());
		}

		public boolean isAccessableResource(Right right) throws AccesssDeniedException {
			return right == null ? true : isAccessableResource(right.id());
		}

		/* Error */
		public int authenticatePassword(
				org.luisyang.framework.http.session.authentication.PasswordAuthentication authentication)
						throws AuthenticationException {
							return accountId;
		}

		/* Error */
		public int checkIn(HttpServletRequest request, HttpServletResponse response,
				org.luisyang.framework.http.session.authentication.PasswordAuthentication authentication)
						throws AuthenticationException {
							return accountId;
		}

		protected void register(int accountId) {
			this.accountId = accountId;
		}

		public boolean isAccessableResource(String resourceId) {
			if (this.accountId <= 0) {
				return false;
			}
			try {
				ServiceProvider serviceProvider = (ServiceProvider) AbstractSessionManager.this.resourceProvider
						.getResource(ServiceProvider.class);
				ServiceSession serviceSession = serviceProvider.createServiceSession(this);
				Throwable localThrowable2 = null;
				try {
					RightManage rightManage = (RightManage) serviceSession.getService(RightManage.class);
					return rightManage.hasRight(this.accountId, resourceId);
				} catch (Throwable localThrowable1) {
					localThrowable2 = localThrowable1;
					throw localThrowable1;
				} finally {
					if (serviceSession != null) {
						if (localThrowable2 != null) {
							try {
								serviceSession.close();
							} catch (Throwable x2) {
								localThrowable2.addSuppressed(x2);
							}
						} else {
							serviceSession.close();
						}
					}
				}
			} catch (Throwable throwable) {
				AbstractSessionManager.this.resourceProvider.log(throwable);
			}
			return false;
		}

		public int getAccountId() throws AuthenticationException {
			if (this.accountId <= 0) {
				throw new AuthenticationException();
			}
			return this.accountId;
		}

		public boolean isAuthenticated() {
			return this.accountId > 0;
		}
	}

	public void initilize(Connection connection) throws Throwable {
		Statement stmt = connection.createStatement();
		Throwable localThrowable2 = null;
		try {
			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1020 (F01 int(10) unsigned NOT NULL AUTO_INCREMENT,F02 varchar(45) NOT NULL,F03 varchar(200) DEFAULT NULL,F04 datetime NOT NULL,F05 int(10) unsigned NOT NULL,F06 enum('QY','TY') NOT NULL,PRIMARY KEY (F01), UNIQUE KEY F02 (F02), KEY F06 (F06)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1021 (F01 int(10) unsigned NOT NULL,F02 varchar(45) NOT NULL,PRIMARY KEY (F01,F02)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1022 (F01 int(10) unsigned NOT NULL,F02 int(10) unsigned NOT NULL,PRIMARY KEY (F01,F02)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1023 (F01 int(10) unsigned NOT NULL,F02 varchar(45) NOT NULL,PRIMARY KEY (F01,F02)) DEFAULT CHARSET=utf8");
			stmt.executeBatch();
		} catch (Throwable localThrowable1) {
			localThrowable2 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (stmt != null) {
				if (localThrowable2 != null) {
					try {
						stmt.close();
					} catch (Throwable x2) {
						localThrowable2.addSuppressed(x2);
					}
				} else {
					stmt.close();
				}
			}
		}
	}
}
