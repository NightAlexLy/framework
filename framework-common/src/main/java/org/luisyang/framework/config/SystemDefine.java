package org.luisyang.framework.config;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.http.session.authentication.AuthenticationException;
import org.luisyang.framework.http.session.authentication.PasswordAuthentication;
import org.luisyang.framework.resource.Resource;

public abstract class SystemDefine {

	public boolean isStrictMVC() {
		return false;
	}

	public abstract String getGUID();

	public abstract String getSystemName();

	public abstract String getSystemDescription();

	public abstract Rewriter getRewriter();

	public abstract int readAccountId(ServiceSession paramServiceSession, String paramString1, String paramString2)
			throws AuthenticationException, SQLException;

	public abstract void writeLog(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, ServiceSession paramServiceSession,
			PasswordAuthentication paramPasswordAuthentication, int paramInt);

	public abstract String getDataProvider(Class<? extends Resource> paramClass);

	public abstract String getSchemaName(Class<? extends Resource> paramClass);

	public abstract String getSiteDomainKey();

	public abstract String getSiteIndexKey();

	public int getMaxErrorTimes() {
		return 10;
	}

	public int getVerifyCodeLength() {
		return 6;
	}
}
