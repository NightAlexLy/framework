package org.luisyang.framework.message;

import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceProvider;

/**
 * 消息供应商
 */
public abstract class MessageProvider extends Resource {

	/**
	 * 初始化
	 * @param resourceProvider
	 */
	public MessageProvider(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 唯一标识
	 */
	public final Class<? extends Resource> getIdentifiedType() {
		return MessageProvider.class;
	}
}
