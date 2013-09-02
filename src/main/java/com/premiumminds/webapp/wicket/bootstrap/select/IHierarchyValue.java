package com.premiumminds.webapp.wicket.bootstrap.select;

import java.io.Serializable;
import java.util.Collection;

@SuppressWarnings("rawtypes")
public interface IHierarchyValue<T extends IHierarchyValue> extends Serializable {
	public Collection<T> getChilds();
	public T getParent();
}
