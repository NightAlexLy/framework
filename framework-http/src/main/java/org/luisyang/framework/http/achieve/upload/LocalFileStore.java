package org.luisyang.framework.http.achieve.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.http.upload.FileInformation;
import org.luisyang.framework.http.upload.FileStore;
import org.luisyang.framework.http.upload.UploadFile;
import org.luisyang.framework.resource.ResourceAnnotation;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRetention;
import org.luisyang.util.StringHelper;

@ResourceAnnotation({ ResourceRetention.PRE_PRODUCTION, ResourceRetention.PRODUCTION })
public class LocalFileStore extends FileStore {
	protected final char separatorChar;
	protected final Pattern pattern;
	protected final String prefix;

	public LocalFileStore(ResourceProvider resourceProvider) {
		super(resourceProvider);
		this.separatorChar = File.separatorChar;
		StringBuilder builder = new StringBuilder();
		builder.append(resourceProvider.getContextPath());
		builder.append(getUploadURI());
		builder.append('/');
		this.prefix = builder.toString();
		builder.append("\\d+/\\d+/\\d+/\\d+/\\d+(\\.\\w+)?");
		this.pattern = Pattern.compile(builder.toString());
		log();
	}

	private void log() {
		File file = getHome();
		String contextPath = this.resourceProvider.getContextPath();
		this.resourceProvider.log(String.format("部署路径: %s, 运行模式: %s, 上传文件存储路径: %s, 上传文件映射路径: %s.",
				new Object[] { StringHelper.isEmpty(contextPath) ? "/" : contextPath,
						this.resourceProvider.getResourceRetention().name(), file == null ? "无" : file.getPath(),
						getUploadURI() }));
	}

	public String newCode(int type, String suffix) throws Throwable {
		if (type < 0) {
			type = 0;
		}
		SystemDefine systemDefine = this.resourceProvider.getSystemDefine();
		Connection connection = ((SQLConnectionProvider) this.resourceProvider
				.getDataConnectionProvider(SQLConnectionProvider.class, systemDefine.getDataProvider(FileStore.class)))
						.getConnection(systemDefine.getSchemaName(FileStore.class));
		Throwable localThrowable6 = null;
		int fileId;
		int day;
		int month;
		int year;
		try {
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO _1050 SET F03 = ?, F04 = ?", 1);
			Throwable localThrowable7 = null;
			Throwable localThrowable8;
			try {
				pstmt.setInt(1, type);
				pstmt.setString(2, suffix);
				pstmt.execute();
				ResultSet resultSet = pstmt.getGeneratedKeys();
				localThrowable8 = null;
				try {
					if (resultSet.next()) {
						fileId = resultSet.getShort(1);
					} else {
						throw new SQLException("生成文件ID失败.");
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
			pstmt = connection
					.prepareStatement("SELECT YEAR(F02), MONTH(F02), DAY(F02) FROM _1050 WHERE F01 = ?");
			localThrowable7 = null;
			try {
				pstmt.setInt(1, fileId);
				ResultSet resultSet = pstmt.executeQuery();
				localThrowable8 = null;
				try {
					if (resultSet.next()) {
						year = resultSet.getShort(1);
						month = resultSet.getShort(2);
						day = resultSet.getShort(3);
					} else {
						throw new SQLException("生成文件ID失败.");
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
		StringBuilder builder = new StringBuilder();
		builder.append(Integer.toHexString(year)).append('-').append(Integer.toHexString(month)).append('-')
				.append(Integer.toHexString(day)).append('-').append(Integer.toHexString(type < 0 ? 0 : type))
				.append('-').append(Integer.toHexString(fileId));
		if ((!StringHelper.isEmpty(suffix)) && (!".".equals(suffix))) {
			if (suffix.charAt(0) != '.') {
				builder.append('.');
			}
			builder.append(suffix);
		}
		return builder.toString();
	}

	public FileInformation getFileInformation(String fileCode) {
		int end = fileCode.lastIndexOf('.');
		final String suffix;
		if (end != -1) {
			if (end + 1 < fileCode.length()) {
				suffix = fileCode.substring(end + 1);
			} else {
				suffix = null;
			}
			fileCode = fileCode.substring(0, end);
		} else {
			suffix = null;
		}
		String[] items = fileCode.split("-");
		if (items.length != 5) {
			return null;
		}
		try {
			final int year = Integer.parseInt(items[0], 16);
			final int month = Integer.parseInt(items[1], 16);
			final int day = Integer.parseInt(items[2], 16);
			final int type = Integer.parseInt(items[3], 16);
			final int id = Integer.parseInt(items[4], 16);
			new FileInformation() {
				public int getYear() {
					return year;
				}

				public int getType() {
					return type;
				}

				public String getSuffix() {
					return suffix;
				}

				public int getMonth() {
					return month;
				}

				public int getId() {
					return id;
				}

				public int getDay() {
					return day;
				}
			};
		} catch (Throwable t) {
		}
		return null;
	}

	public String getURL(FileInformation fileInformation) {
		if (fileInformation == null) {
			return "";
		}
		StringBuilder url = new StringBuilder();
		url.append(this.resourceProvider.getContextPath()).append(getUploadURI()).append('/')
				.append(fileInformation.getType()).append('/').append(fileInformation.getYear()).append('/')
				.append(fileInformation.getMonth()).append('/').append(fileInformation.getDay()).append('/')
				.append(fileInformation.getId());

		String subffix = fileInformation.getSuffix();
		if (!StringHelper.isEmpty(subffix)) {
			url.append('.').append(subffix);
		}
		return url.toString();
	}

	private File getFile(FileInformation fileInformation) throws IOException {
		StringBuilder uri = new StringBuilder();
		uri.append(fileInformation.getType()).append(this.separatorChar).append(fileInformation.getYear())
				.append(this.separatorChar).append(fileInformation.getMonth()).append(this.separatorChar)
				.append(fileInformation.getDay());

		File path = new File(getHome(), uri.toString());
		path.mkdirs();
		uri.setLength(0);
		uri.append(fileInformation.getId());
		String subffix = fileInformation.getSuffix();
		if (!StringHelper.isEmpty(subffix)) {
			uri.append('.').append(subffix);
		}
		return new File(path, uri.toString());
	}

	public void write(FileInformation fileInformation, InputStream inputStream) throws IOException {
		if (inputStream.available() <= 0) {
			return;
		}
		FileOutputStream outputStream = new FileOutputStream(getFile(fileInformation));
		Throwable localThrowable4 = null;
		try {
			if ((inputStream instanceof FileInputStream)) {
				FileChannel in = ((FileInputStream) inputStream).getChannel();
				Throwable localThrowable5 = null;
				try {
					FileChannel out = outputStream.getChannel();
					Throwable localThrowable6 = null;
					try {
						in.transferTo(in.position(), in.size(), out);
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
			} else {
				byte[] buf = new byte[8192];
				int len = inputStream.read(buf);
				while (len > 0) {
					outputStream.write(buf, 0, len);
					len = inputStream.read(buf);
				}
			}
		} catch (Throwable localThrowable3) {
			localThrowable4 = localThrowable3;
			throw localThrowable3;
		} finally {
			if (outputStream != null) {
				if (localThrowable4 != null) {
					try {
						outputStream.close();
					} catch (Throwable x2) {
						localThrowable4.addSuppressed(x2);
					}
				} else {
					outputStream.close();
				}
			}
		}
	}

	public void read(FileInformation fileInformation, OutputStream outputStream) throws IOException {
		FileInputStream inputStream = new FileInputStream(getFile(fileInformation));
		Throwable localThrowable4 = null;
		try {
			if ((outputStream instanceof FileOutputStream)) {
				FileChannel in = inputStream.getChannel();
				Throwable localThrowable5 = null;
				try {
					FileChannel out = ((FileOutputStream) outputStream).getChannel();
					Throwable localThrowable6 = null;
					try {
						in.transferTo(in.position(), in.size(), out);
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
			} else {
				byte[] buf = new byte[8192];
				int len = inputStream.read(buf);
				while (len > 0) {
					outputStream.write(buf, 0, len);
					len = inputStream.read(buf);
				}
			}
		} catch (Throwable localThrowable3) {
			localThrowable4 = localThrowable3;
			throw localThrowable3;
		} finally {
			if (inputStream != null) {
				if (localThrowable4 != null) {
					try {
						inputStream.close();
					} catch (Throwable x2) {
						localThrowable4.addSuppressed(x2);
					}
				} else {
					inputStream.close();
				}
			}
		}
	}

	public String[] upload(int type, UploadFile... uploadFiles) throws Throwable {
		if ((uploadFiles == null) || (uploadFiles.length == 0)) {
			return null;
		}
		ArrayList<String> codes = null;
		for (UploadFile uploadFile : uploadFiles) {
			if (uploadFile != null) {
				InputStream inputStream = uploadFile.getInputStream();
				Throwable localThrowable2 = null;
				try {
					if (inputStream.available() <= 0) {
						if (inputStream != null) {
							if (localThrowable2 != null) {
								try {
									inputStream.close();
								} catch (Throwable x2) {
									localThrowable2.addSuppressed(x2);
								}
							} else {
								inputStream.close();
							}
						}
					} else {
						String code = newCode(type, uploadFile.getSuffix());
						write(getFileInformation(code), inputStream);
						if (codes == null) {
							codes = new ArrayList();
						}
						codes.add(code);
					}
				} catch (Throwable localThrowable1) {
					localThrowable2 = localThrowable1;
					throw localThrowable1;
				} finally {
					if (inputStream != null) {
						if (localThrowable2 != null) {
							try {
								inputStream.close();
							} catch (Throwable x2) {
								localThrowable2.addSuppressed(x2);
							}
						} else {
							inputStream.close();
						}
					}
				}
			}
		}
		return (codes == null) || (codes.size() == 0) ? null : (String[]) codes.toArray(new String[codes.size()]);
	}

	public String encode(String value) {
		if (StringHelper.isEmpty(value)) {
			return value;
		}
		StringBuilder out = new StringBuilder();
		Matcher matcher = this.pattern.matcher(value);
		int startIndex = 0;
		int endIndex = 0;

		StringBuilder builder = new StringBuilder();
		while (matcher.find()) {
			endIndex = matcher.start();
			if (endIndex != startIndex) {
				out.append(value, startIndex, endIndex);
			}
			String v = matcher.group();
			try {
				String key = v.substring(this.prefix.length());
				int suffixIndex = key.lastIndexOf('.');
				String suffix;
				if (suffixIndex != -1) {
					suffix = key.substring(suffixIndex);
					key = key.substring(0, suffixIndex);
				} else {
					suffix = null;
				}
				String[] items = key.split("/");
				int type = Integer.parseInt(items[0]);
				int year = Integer.parseInt(items[1]);
				int month = Integer.parseInt(items[2]);
				int day = Integer.parseInt(items[3]);
				int fileId = Integer.parseInt(items[4]);
				builder.setLength(0);
				builder.append("${");
				builder.append(Integer.toHexString(year)).append('-').append(Integer.toHexString(month)).append('-')
						.append(Integer.toHexString(day)).append('-').append(Integer.toHexString(type < 0 ? 0 : type))
						.append('-').append(Integer.toHexString(fileId));
				if (!StringHelper.isEmpty(suffix)) {
					builder.append(suffix);
				}
				builder.append('}');
				out.append(builder.toString());
			} catch (Throwable t) {
				out.append(v);
			}
			startIndex = matcher.end();
		}
		endIndex = value.length();
		if (startIndex < endIndex) {
			out.append(value, startIndex, endIndex);
		}
		return out.toString();
	}

	public void initilize(Connection connection) throws Throwable {
		Statement stmt = connection.createStatement();
		Throwable localThrowable2 = null;
		try {
			stmt.addBatch(
					"CREATE TABLE IF NOT EXISTS _1050 (F01 int(10) unsigned NOT NULL AUTO_INCREMENT,F02 timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,F03 int(10) unsigned NOT NULL, F04 varchar(20) DEFAULT NULL, PRIMARY KEY (F01));");
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
