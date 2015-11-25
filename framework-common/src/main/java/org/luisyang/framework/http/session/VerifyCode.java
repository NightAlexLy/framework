package org.luisyang.framework.http.session;

/**
 * 验证码接口
 * 
 * @author LuisYang
 */
public abstract interface VerifyCode
{
  /**
   * 获得显示的验证码值
   * @return
   */
  public abstract String getDisplayValue();
  
  /**
   * 获得匹配的验证码值
   * @return
   */
  public abstract String getMatchValue();
}
