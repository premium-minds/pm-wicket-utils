package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import org.apache.wicket.markup.html.form.FormComponent;

public interface IBootstrapCrudifierForm<T> {
	public FormComponent<?> getFormComponent(String propertyName);
}
