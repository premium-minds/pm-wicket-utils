package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CrudifierEntitySettings implements Serializable {
	private static final long serialVersionUID = 2254390543916157835L;

	private Set<String> hiddenFields = new HashSet<String>();
	private Set<String> orderOfFields = new HashSet<String>();

	public Set<String> getHiddenFields() {
		return hiddenFields;
	}
	public Set<String> getOrderOfFields() {
		return orderOfFields;
	}
}
