package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public abstract class EntityProvider<T> implements Serializable {
	private static final long serialVersionUID = -6207101592626765797L;

	public abstract List<T> load();
	
	public IChoiceRenderer<T> getRenderer(){
		return new ChoiceRenderer<T>();
	}
}
