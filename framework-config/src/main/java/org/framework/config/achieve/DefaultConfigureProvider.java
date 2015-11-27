package org.framework.config.achieve;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.config.Envionment;
import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.util.StringHelper;

/**
 * 默认配置供应商
 * 
 * @author LuisYang
 */
public class DefaultConfigureProvider extends ConfigureProvider {
	public static final Map<String, String> CACHE = new WeakHashMap();
	private InetSocketAddress groupAddress;
	private ConfigureMonitor monitor = null;
	private transient boolean notClose = true;

	/**
	 * 初始化
	 * @param resourceProvider
	 */
	public DefaultConfigureProvider(ResourceProvider resourceProvider) {
		super(resourceProvider);
		try {
			this.groupAddress = new InetSocketAddress(InetAddress.getByName("225.1.2.3"), 7788);

			this.monitor = new ConfigureMonitor();
			this.monitor.start();
		} catch (UnknownHostException e) {
			this.groupAddress = null;
		}
	}

	/**
	 * 关闭
	 */
	public void close() throws Exception {
		if (this.notClose) {
			this.notClose = false;
			this.monitor.interrupt();
			this.monitor = null;
		}
		super.close();
	}

	/**
	 * 配置监听
	 * 
	 * @author LuisYang
	 */
	private class ConfigureMonitor extends Thread {
		private ConfigureMonitor() {
		}

		public void run() {
			try {
				MulticastSocket socket = new MulticastSocket(DefaultConfigureProvider.this.groupAddress.getPort());
				Throwable localThrowable2 = null;
				try {
					socket.joinGroup(DefaultConfigureProvider.this.groupAddress.getAddress());
					DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
					while (DefaultConfigureProvider.this.notClose) {
						try {
							socket.receive(packet);
							byte[] buf = packet.getData();
							String key = new String(buf, packet.getOffset(), packet.getLength());

							DefaultConfigureProvider.CACHE.remove(key);
							DefaultConfigureProvider.this.resourceProvider
									.log(String
											.format("上下文: %s 收到来自: %s 通知,配置参数  %s 已失效.",
													new Object[] {
															DefaultConfigureProvider.this.resourceProvider
																	.getContextPath(),
															packet.getAddress().getHostAddress(), key }));
						} catch (Throwable e) {
							DefaultConfigureProvider.this.resourceProvider.log(e);
						}
					}
					socket.leaveGroup(DefaultConfigureProvider.this.groupAddress.getAddress());
				} catch (Throwable localThrowable1) {
					localThrowable2 = localThrowable1;
					try {
						throw localThrowable1;
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} finally {
					if (socket != null) {
						if (localThrowable2 != null) {
							try {
								socket.close();
							} catch (Throwable x2) {
								localThrowable2.addSuppressed(x2);
							}
						} else {
							socket.close();
						}
					}
				}
			} catch (IOException e) {
				DefaultConfigureProvider.this.resourceProvider.log(e);
			}
		}
	}

	/**
	 * 验证属性
	 */
	public void invalidProperty(String key) {
		if ((this.groupAddress == null) || (StringHelper.isEmpty(key))) {
			return;
		}
		try {
			byte[] buf = key.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, this.groupAddress);

			MulticastSocket socket = new MulticastSocket(this.groupAddress.getPort());
			Throwable localThrowable2 = null;
			try {
				socket.joinGroup(this.groupAddress.getAddress());
				socket.send(packet);
				socket.leaveGroup(this.groupAddress.getAddress());
			} catch (Throwable localThrowable1) {
				localThrowable2 = localThrowable1;
				throw localThrowable1;
			} finally {
				if (socket != null) {
					if (localThrowable2 != null) {
						try {
							socket.close();
						} catch (Throwable x2) {
							localThrowable2.addSuppressed(x2);
						}
					} else {
						socket.close();
					}
				}
			}
		} catch (Throwable throwable) {
			this.resourceProvider.log(throwable);
		}
	}

	/**
	 * 获得数据库链接
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws SQLException
	 */
	private Connection getConnection() throws ResourceNotFoundException, SQLException {
		SystemDefine systemDefine = this.resourceProvider.getSystemDefine();
		return ((SQLConnectionProvider) this.resourceProvider.getDataConnectionProvider(SQLConnectionProvider.class,
				systemDefine.getDataProvider(ConfigureProvider.class)))
						.getConnection(systemDefine.getSchemaName(ConfigureProvider.class));
	}

	/**
	 * 配置集合初始化加载
	 */
	void preload() {
		try {
			Connection connection = getConnection();
			Throwable localThrowable4 = null;
			try {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02 FROM _1010");
				try {
					ResultSet resultSet = pstmt.executeQuery();
					try {
						while (resultSet.next()) {
							CACHE.put(resultSet.getString(1), resultSet.getString(2));
						}
					} catch (Throwable localThrowable1) {
						throw localThrowable1;
					} finally {
					}
				} catch (Throwable localThrowable2) {
					throw localThrowable2;
				} finally {
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
		} catch (Throwable e) {
			this.resourceProvider.log(e);
		}
	}

	/**
	 * 获得属性， 如果没有。则从数据库中获取
	 */
	public String getProperty(String key) {
		if (StringHelper.isEmpty(key)) {
			return null;
		}
		if (CACHE.containsKey(key)) {
			return (String) CACHE.get(key);
		}
		String value = null;
		try {
			Connection connection = getConnection();
			Throwable localThrowable4 = null;
			try {
				PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1010 WHERE F01 = ?");
				try {
					pstmt.setString(1, key);
					ResultSet resultSet = pstmt.executeQuery();
					try {
						if (resultSet.next()) {
							value = resultSet.getString(1);
						}
					} catch (Throwable localThrowable1) {
						throw localThrowable1;
					} finally {
					}
				} catch (Throwable localThrowable2) {
					throw localThrowable2;
				} finally {
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
		} catch (Throwable e) {
			this.resourceProvider.log(e);
		}
		if (!StringHelper.isEmpty(value)) {
			CACHE.put(key, value);
		}
		return value;
	}

	/**
	 * 修改属性
	 */
	public void setProperty(String key, String value) {
		if ((StringHelper.isEmpty(key)) || (StringHelper.isEmpty(value))) {
			return;
		}
		try {
			Connection connection = getConnection();
			Throwable localThrowable3 = null;
			try {
				PreparedStatement pstmt = connection.prepareStatement("UPDATE _1010 SET F02 = ? WHERE F01 = ?");
				Throwable localThrowable4 = null;
				try {
					pstmt.setString(1, value);
					pstmt.setString(2, key);
					pstmt.executeUpdate();
					invalidProperty(key);
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
			this.resourceProvider.log(e);
		}
		CACHE.put(key, value);
	}

	public Envionment createEnvionment() {
		return new Envionment() {
			Map<String, String> local = new HashMap();

			public void set(String key, String value) {
				if (StringHelper.isEmpty(key)) {
					return;
				}
				this.local.put(key, value);
			}

			public String get(String key) {
				if (this.local.containsKey(key)) {
					return (String) this.local.get(key);
				}
				return DefaultConfigureProvider.this.getProperty(key);
			}
		};
	}

	public Envionment createEnvionment(final Envionment envionment) {
		return new Envionment() {
			Map<String, String> local = new HashMap();

			public void set(String key, String value) {
				if (StringHelper.isEmpty(key)) {
					return;
				}
				this.local.put(key, value);
			}

			public String get(String key) {
				if (this.local.containsKey(key)) {
					return (String) this.local.get(key);
				}
				return envionment == null ? DefaultConfigureProvider.this.getProperty(key) : envionment.get(key);
			}
		};
	}

	public Envionment createEnvionment(final Map<String, String> values) {
		return new Envionment() {
			Map<String, String> local = new HashMap();

			public void set(String key, String value) {
				if (StringHelper.isEmpty(key)) {
					return;
				}
				this.local.put(key, value);
			}

			public String get(String key) {
				if (this.local.containsKey(key)) {
					return (String) this.local.get(key);
				}
				return DefaultConfigureProvider.this.getProperty(key);
			}
		};
	}

	/**
	 * 初始化表
	 */
	public void initilize(Connection connection) throws Throwable {
		Statement stmt = connection.createStatement();
		Throwable localThrowable2 = null;
		try {
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS _1010 (F01 varchar(100) NOT NULL,F02 text NOT NULL,F03 varchar(100) NOT NULL,F04 text DEFAULT NULL,PRIMARY KEY (F01), KEY F03 (F03) USING BTREE) DEFAULT CHARSET=utf8");
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