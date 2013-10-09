package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CrudifierEntitySettings implements Serializable {
	private static final long serialVersionUID = 2254390543916157835L;
	
	public static enum GridSize { COL1, COL2, COL3, COL4, COL5, COL6, COL7, COL8, COL9, COL10 }

	private Set<String> hiddenFields = new HashSet<String>();
	private Set<String> orderOfFields = new HashSet<String>();
	private Map<String, GridSize> gridFieldsSizes = new HashMap<String, GridSize>();

	public Set<String> getHiddenFields() {
		return hiddenFields;
	}
	public Set<String> getOrderOfFields() {
		return orderOfFields;
	}
	
	public Map<String, GridSize> getGridFieldsSizes(){
		return gridFieldsSizes;
	}
}
