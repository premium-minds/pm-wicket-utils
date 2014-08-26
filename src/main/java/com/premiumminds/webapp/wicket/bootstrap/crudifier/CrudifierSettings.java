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
package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CrudifierSettings implements Serializable {
	private static final long serialVersionUID = -3411337073690438157L;

	private boolean showReset = true;
	private boolean withSelfFeedback = true;
	
	private Map<String, EntityProvider<?>> providers;
	private Set<String> hiddenFields;
	private Set<String> orderOfFields;
	
	public CrudifierSettings(){
		providers = new HashMap<String, EntityProvider<?>>();
		hiddenFields = new HashSet<String>();
		orderOfFields = new LinkedHashSet<String>();
	}
	
	public Map<String, EntityProvider<?>> getProviders(){
		return providers;
	}
	
	public Set<String> getHiddenFields(){
		return hiddenFields;
	}
	
	public Set<String> getOrderOfFields(){
		return orderOfFields;
	}

	public boolean isShowReset() {
		return showReset;
	}

	public void setShowReset(boolean showReset) {
		this.showReset = showReset;
	}

	public boolean isWithSelfFeedback() {
		return withSelfFeedback;
	}

	public void setWithSelfFeedback(boolean withSelfFeedback) {
		this.withSelfFeedback = withSelfFeedback;
	}
}
