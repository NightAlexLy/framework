package org.luisyang.framework.http.session.authentication;

/**
 * 密码认证模型 
 * 
 */
public class PasswordAuthentication extends VerifyCodeAuthentication {
	
	private static final long serialVersionUID = 4450809801080671635L;
	/**
	 * 用户名
	 */
	protected String accountName;
	/**
	 * 密码
	 */
	protected String password;

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
