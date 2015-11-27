package org.luisyang.framework.message.achieve;

import java.sql.Connection;
import java.sql.Statement;

import org.luisyang.framework.message.MessageProvider;
import org.luisyang.framework.resource.ResourceProvider;

public class DefaultMessageProvider extends MessageProvider {
	public DefaultMessageProvider(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	public void initilize(Connection connection) throws Throwable {
		Statement stmt = connection.createStatement();
		Throwable localThrowable2 = null;
		try {
			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1040(`F01`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT ,`F02` int(4) NULL DEFAULT 0,`F03`  text NOT NULL ,`F04`  datetime NOT NULL ,`F05`  enum('W','Z') NULL DEFAULT 'W',`F06` timestamp NULL DEFAULT NULL ,PRIMARY KEY (`F01`)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1041(`F01`  bigint(20) UNSIGNED NOT NULL ,`F02`  varchar(20) NULL) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1042(`F01`  bigint(20) UNSIGNED NOT NULL ,`F02`  int NOT NULL ,`F03`  text NOT NULL ,`F04`  datetime NOT NULL ,`F05`  enum('YES','NO') NULL DEFAULT 'YES',`F06` text NULL,PRIMARY KEY (`F01`)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1046(`F01`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT ,`F02`  varchar(200) NOT NULL ,`F03`  text NOT NULL ,`F04`  int(4) NULL DEFAULT 0,`F05` datetime NOT NULL,`F06` timestamp NULL DEFAULT NULL,`F07` enum('Z','W') NOT NULL DEFAULT 'W' ,PRIMARY KEY (`F01`)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1047(`F01`  bigint(20) UNSIGNED NOT NULL ,`F02`  varchar(60) NULL) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1048(`F01`  bigint(20) UNSIGNED NOT NULL ,`F02`  varchar(200) NOT NULL ,`F03`  text NOT NULL ,`F04`  int(4) NULL DEFAULT 0,`F05`  datetime NOT NULL,PRIMARY KEY (`F01`)) DEFAULT CHARSET=utf8");

			stmt.addBatch(
					"CREATE EVENT IF NOT EXISTS `_minute_sp_1040` ON SCHEDULE EVERY 1 MINUTE STARTS '2014-01-01 00:30:00' ON COMPLETION PRESERVE ENABLE DO UPDATE _1040 SET F05 = 'W', F06 = NULL WHERE F06 <= CURRENT_TIMESTAMP();");
			stmt.addBatch(
					"CREATE EVENT IF NOT EXISTS `_minute_sp_1046` ON SCHEDULE EVERY 1 MINUTE STARTS '2014-01-01 00:00:00' ON COMPLETION PRESERVE ENABLE DO UPDATE _1040 SET F07 = 'W', F06 = NULL WHERE F06 <= CURRENT_TIMESTAMP();");
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
