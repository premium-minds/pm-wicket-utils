package com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierEntitySettings;


public interface ControlGroupProvider<T extends AbstractControlGroup<?>> extends Serializable {
	public T createControlGroup(String id, IModel<?> model, String name, Component component, boolean required, Class<?> type, CrudifierEntitySettings entitySettings);
}
