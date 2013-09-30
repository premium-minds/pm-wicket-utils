package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class PropertyColumn<T> implements IColumn<T>, Serializable {
	private static final long serialVersionUID = -1539532027947493672L;

	private String property;
	private ColumnAlign align;
	
	public PropertyColumn(String property){
		this(property, ColumnAlign.LEFT);
	}
	
	public PropertyColumn(String property, ColumnAlign align){
		this.property = property;
		this.align = align;
	}

	public IModel<Object> getPropertyModel(T object) {
		return new PropertyModel<Object>(object, property);
	}

	public ColumnAlign getAlign() {
		return align;
	}

	public String getPropertyName() {
		return property;
	}

}
