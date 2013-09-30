package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.io.Serializable;

public interface IObjectRenderer<T> extends Serializable {
	public String render(T object);
}
