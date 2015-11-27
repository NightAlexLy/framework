package org.luisyang.framework.config.service;

import org.luisyang.framework.config.entity.VariableBean;
import org.luisyang.framework.service.Service;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;

public  abstract interface VariableManage extends Service{
	
	 /**
	  * 根据变量名获得变量值
	  * @param key 变量主键
	  * @return
	  * @throws Throwable
	  */
	 public abstract String getProperty(String key)
			    throws Throwable;
	 
	 /**
	  * 根据变量名修改变量值
	  * @param key
	  * @param newValue
	  * @throws Throwable
	  */
	 public abstract void setProperty(String key, String newValue)
			    throws Throwable;
	
	 /**
	  * 根据条件搜索指定的变量模型
	  * @param paramVariableQuery
	  * @param paramPaging
	  * @return
	  * @throws Throwable
	  */
	public abstract PagingResult<VariableBean> search(VariableQuery paramVariableQuery, Paging paramPaging)
			    throws Throwable;
	 
	 /**
	  * 获得变量模型
	  * @param key 变量主键
	  * @return
	  * @throws Throwable
	  */
	 public abstract VariableBean get(String key)
			    throws Throwable;
			  
	 /**
	  * 同步
	  * @throws Throwable
	  */
	 public abstract void synchronize()
			    throws Throwable;
	 
	 /**
	  * 重置
	  * @throws Throwable
	  */
	 public abstract void reset()
			    throws Throwable;
	
	 /**
	  * 变量查询对象<br/>
	  * 	参考 变量模型Bean
	  */
	 public static abstract interface VariableQuery
	 {
		 public abstract String getKey();
			    
		 public abstract String getValue();
			    
		 public abstract String getType();
			    
		 public abstract String getDescription();
	}
}
