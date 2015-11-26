package org.luisyang.framework.config;

import java.io.IOException;
import java.util.Map;

import org.luisyang.framework.config.entity.VariableBean;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.util.StringHelper;

/**
 * 配置供应商
 * 
 * @author LuisYang
 */
public abstract class ConfigureProvider extends Resource implements Envionment {
	
	public ConfigureProvider(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 唯一标识类型
	 */
	public final Class<? extends Resource> getIdentifiedType() {
		return ConfigureProvider.class;
	}

	/**
	 * 验证属性
	 * @param paramString
	 */
	public abstract void invalidProperty(String key);

	/**
	 * 获得变量
	 * @param variableBean  变量模型 
	 * @return
	 */
	public String getProperty(VariableBean variableBean) {
		if (variableBean == null) {
			return null;
		}
		String value = getProperty(variableBean.getKey());
		if (StringHelper.isEmpty(value)) {
			value = variableBean.getValue();
		}
		return value;
	}

	/**
	 * 通过key获得变量值
	 * @param paramString
	 * @return
	 */
	public abstract String getProperty(String key);

	/**
	 * 修改变量值
	 * @param key
	 * @param value
	 */
	public abstract void setProperty(String key, String value);

	/**
	 * 创建环境模型
	 * @return
	 */
	public abstract Envionment createEnvionment();

	/**
	 * 创建环境模型
	 * @param envionment
	 * @return
	 */
	public abstract Envionment createEnvionment(Envionment envionment);
	
	/**
	 * 创建环境模型
	 * @param envionment
	 * @return
	 */
	public abstract Envionment createEnvionment(Map<String, String> values);

	/**
	 * 获得变量值
	 */
	public String get(String key) {
		return getProperty(key);
	}

	/**
	 * 修改变量值
	 */
	public void set(String key, String value) {
		setProperty(key, value);
	}

	/**
	 * 格式化
	 * @param out
	 * @param variable
	 * @throws IOException
	 */
	public void format(Appendable out, VariableBean variable) throws IOException {
		if ((out == null) || (variable == null)) {
			return;
		}
		StringHelper.format(out, getProperty(variable.getKey()), this);
	}

	/**
	 * 格式化
	 * @param out
	 * @param variable
	 * @param envionment
	 * @throws IOException
	 */
	public void format(Appendable out, VariableBean variable, Envionment envionment) throws IOException {
		if ((out == null) || (variable == null)) {
			return;
		}
		StringHelper.format(out, getProperty(variable.getKey()), envionment);
	}

	/**
	 * 格式化
	 * @param out
	 * @param key
	 * @throws IOException
	 */
	public void format(Appendable out, String key) throws IOException {
		if ((out == null) || (StringHelper.isEmpty(key))) {
			return;
		}
		StringHelper.format(out, getProperty(key), this);
	}

	/**
	 * 格式化
	 * @param out
	 * @param key
	 * @param envionment
	 * @throws IOException
	 */
	public void format(Appendable out, String key, Envionment envionment) throws IOException {
		if ((out == null) || (envionment == null) || (StringHelper.isEmpty(key))) {
			return;
		}
		StringHelper.format(out, getProperty(key), envionment);
	}

	/**
	 * 获取格式化后的变量值
	 * @param variable
	 * @return
	 * @throws IOException
	 */
	public String format(VariableBean variable) throws IOException {
		if (variable == null) {
			return null;
		}
		return StringHelper.format(getProperty(variable.getKey()), this);
	}

	/**
	 * 获取格式化后的变量值
	 * @param variable
	 * @param envionment
	 * @return
	 * @throws IOException
	 */
	public String format(VariableBean variable, Envionment envionment) throws IOException {
		if (variable == null) {
			return null;
		}
		return StringHelper.format(getProperty(variable.getKey()), envionment);
	}

	/**
	 * 获取格式化后的变量值
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String format(String key) throws IOException {
		if (StringHelper.isEmpty(key)) {
			return null;
		}
		return StringHelper.format(getProperty(key), this);
	}

	/**
	 * 获取格式化后的变量值
	 * @param key
	 * @param envionment
	 * @return
	 * @throws IOException
	 */
	public String format(String key, Envionment envionment) throws IOException {
		if (StringHelper.isEmpty(key)) {
			return null;
		}
		return StringHelper.format(getProperty(key), envionment);
	}
}
