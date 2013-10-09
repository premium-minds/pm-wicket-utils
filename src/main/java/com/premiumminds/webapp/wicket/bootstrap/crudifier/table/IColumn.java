package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import java.util.Map;

import org.apache.wicket.Component;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;

public interface IColumn<T> {
	public String getPropertyName();
	public Component createComponent(String id, T object, Component resourceBase, Map<Class<?>, IObjectRenderer<?>> renderers); 
	public ColumnAlign getAlign();
}
