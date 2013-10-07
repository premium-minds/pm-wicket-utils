package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.premiumminds.webapp.wicket.bootstrap.SpecialDate;

public class CrudifierSettings implements Serializable {
	private static final long serialVersionUID = -3411337073690438157L;

	private boolean showReset = true;
	private boolean withSelfFeedback = true;
	
	private Map<String, EntityProvider<?>> providers;
	private Map<Class<?>, IObjectRenderer<?>> renderers;
	private Set<String> hiddenFields;
	private Set<String> orderOfFields;
	private Collection<SpecialDate> specialDates;
	
	public CrudifierSettings(){
		providers = new HashMap<String, EntityProvider<?>>();
		renderers = new HashMap<Class<?>, IObjectRenderer<?>>();
		hiddenFields = new HashSet<String>();
		orderOfFields = new LinkedHashSet<String>();
		specialDates = new ArrayList<SpecialDate>();
	}
	
	public Map<String, EntityProvider<?>> getProviders(){
		return providers;
	}
	
	public Map<Class<?>, IObjectRenderer<?>> getRenderers(){
		return renderers;
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
	
	public Collection<SpecialDate> getSpecialDates() {
		return this.specialDates;
	}
	
	public void setSpecialDates(Collection<SpecialDate> specialDates) {
		this.specialDates = specialDates;
	}
	
	public void setRenderers(Map<Class<?>, IObjectRenderer<?>> renderers) {
		this.renderers = renderers;
	}
}
