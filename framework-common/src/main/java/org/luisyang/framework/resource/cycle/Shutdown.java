package org.luisyang.framework.resource.cycle;

import org.luisyang.framework.resource.ResourceProvider;

/**
 * 停止 
 */
public abstract interface Shutdown
{
  public abstract void onShutdown(ResourceProvider paramResourceProvider);
}
