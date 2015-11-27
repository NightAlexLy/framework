package org.luisyang.framework.config.achieve;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.cycle.Startup;

public class VariablesPreLoader implements Startup {
	
	public void onStartup(ResourceProvider resourceProvider) {
		ConfigureProvider configureProvider = (ConfigureProvider) resourceProvider.getResource(ConfigureProvider.class);
		if ((configureProvider instanceof DefaultConfigureProvider)) {
			DefaultConfigureProvider provider = (DefaultConfigureProvider) configureProvider;
			provider.preload();
		}
	}
}