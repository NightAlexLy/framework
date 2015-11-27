package org.luisyang.framework.resource.achieve;

import java.util.Enumeration;
import java.util.Hashtable;

import org.luisyang.framework.resource.InitParameterProvider;
import org.luisyang.util.StringHelper;

public class ArgumentInitParameterProvider implements InitParameterProvider {
	protected Hashtable<String, String> parameters = new Hashtable();

	public ArgumentInitParameterProvider(String... arguments) {
		if ((arguments == null) || (arguments.length == 0)) {
			return;
		}
		boolean readKey = true;
		String key = null;
		for (String argument : arguments) {
			if (!StringHelper.isEmpty(argument)) {
				argument = argument.trim();
				if (readKey) {
					if (argument.startsWith("--")) {
						key = argument.substring(2);
					} else if (argument.startsWith("-")) {
						key = argument.substring(1);
					} else {
						key = argument;
					}
					readKey = false;
				} else {
					this.parameters.put(key, argument);
					readKey = true;
				}
			}
		}
	}

	public String getInitParameter(String name) {
		return (String) this.parameters.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return this.parameters.keys();
	}
}