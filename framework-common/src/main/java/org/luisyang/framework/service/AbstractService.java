package org.luisyang.framework.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.luisyang.framework.data.sql.mysql.MySql5PageHepler;
import org.luisyang.framework.resource.AchieveVersion;
import org.luisyang.framework.service.query.ArrayParser;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;

/**
 * <b>抽象服务</b>
 *  <ul>
 *  	<li>execute 快速执行SQL语句</li>
 *  	<li>insert 快速插入</li>
 *  	<li>select and selectAll  提供查询</li>
 *  	<li>selectPaging 提供数据的分页查询</li>
 *  </ul>
 * 
 * @author LuisYang
 */
public abstract class AbstractService implements Service {

	protected final ServiceResource serviceResource;
	protected final Logger logger = Logger.getLogger(getClass());

	public AbstractService(ServiceResource serviceResource) {
		this.serviceResource = serviceResource;
	}

	public int getAchieveVersion() {
		AchieveVersion achieveVersion = (AchieveVersion) getClass().getAnnotation(AchieveVersion.class);

		return achieveVersion == null ? 0 : achieveVersion.version();
	}

	protected void execute(Connection connection, String sql, Object... parameters) throws Throwable {
		PreparedStatement pstmt = connection.prepareStatement(sql);
		Throwable localThrowable2 = null;
		try {
			this.serviceResource.setParameters(pstmt, parameters);
			pstmt.execute();
		} catch (Throwable localThrowable1) {
			localThrowable2 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (pstmt != null) {
				if (localThrowable2 != null) {
					try {
						pstmt.close();
					} catch (Throwable x2) {
						localThrowable2.addSuppressed(x2);
					}
				} else {
					pstmt.close();
				}
			}
		}
	}

	protected void execute(Connection connection, String sql, Collection<Object> parameters) throws Throwable {
		PreparedStatement pstmt = connection.prepareStatement(sql);
		Throwable localThrowable2 = null;
		try {
			this.serviceResource.setParameters(pstmt, parameters);
			pstmt.execute();
		} catch (Throwable localThrowable1) {
			localThrowable2 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (pstmt != null) {
				if (localThrowable2 != null) {
					try {
						pstmt.close();
					} catch (Throwable x2) {
						localThrowable2.addSuppressed(x2);
					}
				} else {
					pstmt.close();
				}
			}
		}
	}

	protected int insert(Connection connection, String sql, Object... parameters) throws SQLException {
		return 0;
	}

	protected int insert(Connection connection, String sql, Collection<Object> parameters) throws SQLException {
		return 0;
	}

	protected <T> T select(Connection connection, org.luisyang.framework.service.query.ItemParser<T> parser, String sql,
			Collection<Object> parameters) throws SQLException {
		return null;
	}

	protected <T> T select(Connection connection, org.luisyang.framework.service.query.ItemParser<T> parser, String sql,
			Object... parameters) throws SQLException {
		return null;
	}

	protected <T> T[] selectAll(Connection connection, ArrayParser<T> parser, String sql, Collection<Object> parameters)
			throws SQLException {
		return null;
	}

	protected <T> T[] selectAll(Connection connection, ArrayParser<T> parser, String sql, Object... parameters)
			throws SQLException {
		return null;
	}

	protected <T> PagingResult<T> selectPaging(Connection connection, ArrayParser<T> parser, Paging paging, String sql,
			Object... parameters) throws Throwable {
		int size = paging == null ? 10 : paging.getSize();
		int currentPage = paging == null ? 1 : paging.getCurrentPage();
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int itemCount = 0;
		int pageCount = 0;
		if (size <= 0) {
			size = 10;
		}
		PreparedStatement pstmt = connection.prepareStatement(MySql5PageHepler.getCountString(sql));
		Throwable localThrowable = null;
		try {
			this.serviceResource.setParameters(pstmt, parameters);
			ResultSet resultSet = pstmt.executeQuery();
			try {
				if (resultSet.next()) {
					itemCount = resultSet.getInt(1);
					pageCount = (int) Math.ceil(itemCount / size);
					if (pageCount <= 0) {
						pageCount = 1;
					}
				} else {
					pageCount = 1;
					itemCount = 0;
					currentPage = 1;
				}
			} catch (Throwable localThrowable1) {
				localThrowable = localThrowable1;
				throw localThrowable1;
			} finally {
			}
		} catch (Throwable localThrowable2) {
			localThrowable = localThrowable2;
			throw localThrowable2;
		} finally {
			if (pstmt != null) {
				if (localThrowable != null) {
					try {
						pstmt.close();
					} catch (Throwable x2) {
						localThrowable.addSuppressed(x2);
					}
				} else {
					pstmt.close();
				}
			}
		}
		if (currentPage > pageCount) {
			currentPage = pageCount;
		}
		pstmt = connection
				.prepareStatement(MySql5PageHepler.getLimitString(sql, (currentPage - 1) * size, size));
		localThrowable = null;
		T[] items;
		try {
			this.serviceResource.setParameters(pstmt, parameters);
			ResultSet resultSet = pstmt.executeQuery();
			localThrowable = null;
			try {
				items = parser.parse(resultSet);
			} catch (Throwable localThrowable8) {
				localThrowable = localThrowable8;
				throw localThrowable8;
			} finally {
			}
		} catch (Throwable localThrowable7) {
			localThrowable = localThrowable7;
			throw localThrowable7;
		} finally {
			if (pstmt != null) {
				if (localThrowable != null) {
					try {
						pstmt.close();
					} catch (Throwable x2) {
						localThrowable.addSuppressed(x2);
					}
				} else {
					pstmt.close();
				}
			}
		}
		return new PagingResultImpl(size, currentPage, itemCount, pageCount, items);
	}

	protected <T> PagingResult<T> selectPaging(Connection connection, ArrayParser<T> parser, Paging paging, String sql,
			Collection<Object> parameters) throws Throwable {
		int size = paging == null ? 10 : paging.getSize();
		int currentPage = paging == null ? 1 : paging.getCurrentPage();
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int itemCount = 0;
		int pageCount = 0;
		if (size <= 0) {
			size = 10;
		}
		StringBuilder buf = new StringBuilder("SELECT COUNT(1) FROM (");
		buf.append(sql).append(") AS _A");
		PreparedStatement pstmt = connection.prepareStatement(buf.toString());
		Throwable localThrowable5 = null;
		Throwable localThrowable6;
		try {
			this.serviceResource.setParameters(pstmt, parameters);
			ResultSet resultSet = pstmt.executeQuery();
			localThrowable6 = null;
			try {
				if (resultSet.next()) {
					itemCount = resultSet.getInt(1);
					pageCount = (int) Math.ceil(itemCount / size);
					if (pageCount <= 0) {
						pageCount = 1;
					}
				} else {
					pageCount = 1;
					itemCount = 0;
					currentPage = 1;
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
			if (pstmt != null) {
				if (localThrowable5 != null) {
					try {
						pstmt.close();
					} catch (Throwable x2) {
						localThrowable5.addSuppressed(x2);
					}
				} else {
					pstmt.close();
				}
			}
		}
		if (currentPage > pageCount) {
			currentPage = pageCount;
		}
		buf.setLength(0);
		buf.append(sql).append(" LIMIT ").append((currentPage - 1) * size).append(", ").append(size);

		String pagingSql = buf.toString();

		pstmt = connection.prepareStatement(pagingSql);
		localThrowable5 = null;
		T[] items;
		try {
			this.serviceResource.setParameters(pstmt, parameters);
			ResultSet resultSet = pstmt.executeQuery();
			localThrowable6 = null;
			try {
				items = parser.parse(resultSet);
			} catch (Throwable localThrowable3) {
				localThrowable6 = localThrowable3;
				throw localThrowable3;
			} finally {
			}
		} catch (Throwable localThrowable4) {
			localThrowable5 = localThrowable4;
			throw localThrowable4;
		} finally {
			if (pstmt != null) {
				if (localThrowable5 != null) {
					try {
						pstmt.close();
					} catch (Throwable x2) {
						localThrowable5.addSuppressed(x2);
					}
				} else {
					pstmt.close();
				}
			}
		}
		return new PagingResultImpl(size, currentPage, itemCount, pageCount, items);
	}

	private class PagingResultImpl<T> implements PagingResult<T> {
		private final int size;
		private final int currentPage;
		private final int itemCount;
		private final int pageCount;
		private final T[] items;

		public PagingResultImpl(int size, int currentPage, int itemCount, int pageCount, T[] items) {
			this.size = size;
			this.currentPage = currentPage;
			this.itemCount = itemCount;
			this.pageCount = pageCount;
			this.items = items;
		}

		public int getCurrentPage() {
			return this.currentPage;
		}

		public int getSize() {
			return this.size;
		}

		public int getItemCount() {
			return this.itemCount;
		}

		public int getPageCount() {
			return this.pageCount;
		}

		public T[] getItems() {
			return this.items;
		}
	}
}
