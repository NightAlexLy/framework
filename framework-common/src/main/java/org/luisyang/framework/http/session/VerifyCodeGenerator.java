package org.luisyang.framework.http.session;

/**
 * 验证码生成器
 * 
 * @author LuisYang
 */
public abstract interface VerifyCodeGenerator
{

  /**
   * 验证码超时时间
   * @return
   */
  public abstract long getTTL();
  
  /**
   * 创建一个验证码生成器
   * @return
   */
  public abstract VerifyCode newVerifyCode();
}
