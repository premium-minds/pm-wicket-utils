package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidationError;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;
import com.premiumminds.webapp.wicket.bootstrap.BootstrapDatepicker;

public class DateControlGroup extends AbstractControlGroup<Date> {
	private static final long serialVersionUID = 7519983535463694024L;

	private DateTextField dateField;
	
	public DateControlGroup(String id, IModel<Date> model) {
		super(id, model);
		
		BootstrapDatepicker datepicker = new BootstrapDatepicker("datepicker"){
			private static final long serialVersionUID = -1294334224980199521L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if(isEnabledInHierarchy()) tag.append("class", "input-append", " ");
			}
		};
		
		dateField = new DateTextField("input", getModel()){
			private static final long serialVersionUID = 4925601760084153117L;

			@Override
			public void error(IValidationError error) {
				MessageSource source = new MessageSource();
				Serializable message = error.getErrorMessage(source);
				
				super.error(message);
			}
		};
		
		datepicker.add(dateField);
		datepicker.add(new WebMarkupContainer("icon"){
			private static final long serialVersionUID = -4412622222987841668L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				//don't display icon if it is disabled
				setVisible(dateField.isEnabledInHierarchy());
			}
		});
		add(new BootstrapControlGroupFeedback("controlGroup").add(datepicker));

		
	}

	@Override
	public FormComponent<Date> getFormComponent() {
		return dateField;
	}

}
