/**
 * Copyright (C) 2014 Premium Minds.
 *
 * This file is part of pm-wicket-utils.
 *
 * pm-wicket-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-wicket-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-wicket-utils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CrudifierEntitySettings implements Serializable {
	private static final long serialVersionUID = 2254390543916157835L;
	
	public static enum GridSize { COL1, COL2, COL3, COL4, COL5, COL6, COL7, COL8, COL9, COL10 }

	private Set<String> hiddenFields = new LinkedHashSet<String>();
	private Set<String> orderOfFields = new LinkedHashSet<String>();
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
