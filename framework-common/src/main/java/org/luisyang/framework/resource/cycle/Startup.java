package org.luisyang.framework.resource.cycle;

import org.luisyang.framework.resource.ResourceProvider;

/**
 * 启动 
 */
public abstract interface Startup
{
  public abstract void onStartup(ResourceProvider paramResourceProvider);
}
