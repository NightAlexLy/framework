package org.luisyang.framework.data.sql;

import java.sql.Connection;

import org.luisyang.framework.data.DataConnection;

/**
 * SQL连接
 * 
 * @author LuisYang
 */
public abstract interface SQLConnection extends Connection, DataConnection {
}
