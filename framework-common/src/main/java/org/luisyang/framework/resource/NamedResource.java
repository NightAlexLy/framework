package org.luisyang.framework.resource;

/**
 * 姓名资源
 * 
 * @author LuisYang
 */
public abstract class NamedResource extends Resource {

	public NamedResource(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	public abstract String getName();
}
