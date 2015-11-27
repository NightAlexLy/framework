package org.luisyang.framework.http.achieve.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.http.upload.FileInformation;
import org.luisyang.framework.http.upload.FileStore;
import org.luisyang.framework.resource.ResourceAnnotation;
import org.luisyang.framework.resource.ResourceProvider;

@ResourceAnnotation({ org.luisyang.framework.resource.ResourceRetention.DEVELOMENT })
public class DBFileStore extends LocalFileStore {
	public DBFileStore(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	public File getHome() {
		return null;
	}

	public void write(FileInformation fileInformation, InputStream inputStream) throws IOException {
		if (inputStream.available() <= 0) {
			return;
		}
		SystemDefine systemDefine = this.resourceProvider.getSystemDefine();
		try {
			Connection connection = ((SQLConnectionProvider) this.resourceProvider.getDataConnectionProvider(
					SQLConnectionProvider.class, systemDefine.getDataProvider(FileStore.class)))
							.getConnection(systemDefine.getSchemaName(FileStore.class));
			Throwable localThrowable3 = null;
			try {
				PreparedStatement pstmt = connection.prepareStatement(
						"INSERT INTO _1051 SET F01 = ?, F02 = ? ON DUPLICATE KEY UPDATE F02 = VALUES(F02)");
				Throwable localThrowable4 = null;
				try {
					pstmt.setInt(1, fileInformation.getId());
					pstmt.setBlob(2, inputStream);
					pstmt.execute();
				} catch (Throwable localThrowable1) {
					localThrowable4 = localThrowable1;
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				localThrowable3 = localThrowable2;
				throw localThrowable2;
			} finally {
				if (connection != null) {
					if (localThrowable3 != null) {
						try {
							connection.close();
						} catch (Throwable x2) {
							localThrowable3.addSuppressed(x2);
						}
					} else {
						connection.close();
					}
				}
			}
		} catch (Throwable e) {
			new IOException(e);
		}
	}

	public void read(FileInformation fileInformation, OutputStream outputStream) throws IOException {
		SystemDefine systemDefine = this.resourceProvider.getSystemDefine();
		try {
			Connection connection = ((SQLConnectionProvider) this.resourceProvider.getDataConnectionProvider(
					SQLConnectionProvider.class, systemDefine.getDataProvider(FileStore.class)))
							.getConnection(systemDefine.getSchemaName(FileStore.class));
			Throwable localThrowable5 = null;
			try {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1051 WHERE F01 = ?");
				Throwable localThrowable6 = null;
				try {
					pstmt.setInt(1, fileInformation.getId());
					ResultSet resultSet = pstmt.executeQuery();
					Throwable localThrowable7 = null;
					try {
						if (resultSet.next()) {
							Blob blob = resultSet.getBlob(1);
							InputStream inputStream = blob.getBinaryStream();
							Throwable localThrowable8 = null;
							try {
								byte[] buf = new byte[8192];
								int len = inputStream.read(buf);
								while (len > 0) {
									outputStream.write(buf, 0, len);
									len = inputStream.read(buf);
								}
							} catch (Throwable localThrowable1) {
								localThrowable8 = localThrowable1;
								throw localThrowable1;
							} finally {
								if (inputStream != null) {
									if (localThrowable8 != null) {
										try {
											inputStream.close();
										} catch (Throwable x2) {
											localThrowable8.addSuppressed(x2);
										}
									} else {
										inputStream.close();
									}
								}
							}
						}
					} catch (Throwable localThrowable2) {
						localThrowable7 = localThrowable2;
						throw localThrowable2;
					} finally {
					}
				} catch (Throwable localThrowable3) {
					localThrowable6 = localThrowable3;
					throw localThrowable3;
				} finally {
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
		} catch (Throwable e) {
			new IOException(e);
		}
	}

	public void initilize(Connection connection) throws Throwable {
		super.initilize(connection);
		Statement stmt = connection.createStatement();
		Throwable localThrowable2 = null;
		try {
			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1051 (F01 int(10) unsigned NOT NULL,F02 longblob NOT NULL,PRIMARY KEY (F01)) DEFAULT CHARSET=utf8;");
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
