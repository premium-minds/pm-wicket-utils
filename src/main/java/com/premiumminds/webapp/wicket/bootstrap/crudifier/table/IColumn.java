package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import org.apache.wicket.model.IModel;

public interface IColumn<T> {
	public String getPropertyName();
	public IModel<Object> getPropertyModel(T object);
	public ColumnAlign getAlign();
}
