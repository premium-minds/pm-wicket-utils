package com.premiumminds.webapp.wicket.bootstrap;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.joda.time.ReadableInstant;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.JodaInstantTextField;
import com.premiumminds.webapp.wicket.bootstrap.datepicker.BootstrapDatePickerBehaviour;

public class BootstrapJodaDatepicker<T extends ReadableInstant> extends WebMarkupContainer {
	private static final long serialVersionUID = -117683073963817461L;

	private JodaInstantTextField<T> dateField;
	
	public BootstrapJodaDatepicker(String id) {
		super(id);
		add(new BootstrapDatePickerBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<SpecialDate> getSpecialDates() {
				return BootstrapJodaDatepicker.this.getSpecialDates();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		for(Component component : visitChildren(JodaInstantTextField.class)){
			dateField = (JodaInstantTextField<T>) component;
			break;
		}
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if(dateField!=null){
			tag.put("data-date-format", dateField.getTextFormat().toLowerCase());
		}
	}
	
	public Collection<SpecialDate> getSpecialDates() {
		return null;
	}
	
}
