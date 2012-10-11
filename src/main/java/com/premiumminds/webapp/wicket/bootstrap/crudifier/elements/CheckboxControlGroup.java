package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;

public class CheckboxControlGroup extends AbstractControlGroup<Boolean> {
	private static final long serialVersionUID = -2510616774931793758L;
	
	private CheckBox checkbox;
	
	public CheckboxControlGroup(String id, IModel<Boolean> model) {
		super(id, model);
		
		checkbox = new CheckBox("input", getModel());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new BootstrapControlGroupFeedback("controlGroup").add(checkbox));
	}
	@Override
	public FormComponent<Boolean> getFormComponent() {
		return checkbox;
	}

}
