package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CrudifierConfiguration implements Serializable {
	private static final long serialVersionUID = -3411337073690438157L;

	private Map<String, EntityProvider<?>> providers;
	
	public CrudifierConfiguration(){
		providers = new HashMap<String, EntityProvider<?>>();
	}
	
	public Map<String, EntityProvider<?>> getProviders(){
		return providers;
	}
}
