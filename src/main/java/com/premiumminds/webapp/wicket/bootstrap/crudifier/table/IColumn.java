package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import org.apache.wicket.Component;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;

public interface IColumn<T> {
	public String getPropertyName();
	public Component createComponent(String id, T object, Component resourceBase, CrudifierSettings settings); 
	public ColumnAlign getAlign();
}
