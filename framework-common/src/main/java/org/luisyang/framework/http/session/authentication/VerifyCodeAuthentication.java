package org.luisyang.framework.http.session.authentication;

import java.io.Serializable;

/**
 * 验证码验证模型
 * 
 * @author LuisYang
 */
public class VerifyCodeAuthentication implements Serializable {
	
	private static final long serialVersionUID = 271402867515240680L;
	/**
	 * 验证码值
	 */
	protected String verifyCode;
	/**
	 * 验证码类型
	 */
	protected String verifyCodeType;

	public String getVerifyCode() {
		return this.verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getVerifyCodeType() {
		return this.verifyCodeType;
	}

	public void setVerifyCodeType(String verifyCodeType) {
		this.verifyCodeType = verifyCodeType;
	}

}
