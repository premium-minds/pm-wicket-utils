package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;
import java.util.List;

public interface EntityProvider<T> extends Serializable {
	public List<T> load();
}
