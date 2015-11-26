package org.luisyang.framework.resource;

/**
 * 名称资源
 * 
 * @author LuisYang
 */
public abstract class NamedResource extends Resource {

	public NamedResource(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 获得名称
	 * @return
	 */
	public abstract String getName();
}
