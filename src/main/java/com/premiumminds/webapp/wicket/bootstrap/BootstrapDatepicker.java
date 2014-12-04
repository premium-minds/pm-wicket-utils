/**
 * Copyright (C) 2014 Premium Minds.
 *
 * This file is part of pm-wicket-utils.
 *
 * pm-wicket-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-wicket-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-wicket-utils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.webapp.wicket.bootstrap;

import java.util.Collection;
import java.util.Date;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.premiumminds.webapp.wicket.bootstrap.datepicker.BootstrapDatePickerBehaviour;

/**
 * Bootstrap Datepicker for Wicket.
 *
 * @author acamilo
 * @see <a href="https://github.com/eternicode/bootstrap-datepicker">https://github.com/eternicode/bootstrap-datepicker</a>
 */
public class BootstrapDatepicker extends WebMarkupContainer implements IGenericComponent<Date> {
	private static final long serialVersionUID = -117683073963817461L;

	private DateTextField dateField;
	
	/**
	 * Instantiates a new bootstrap datepicker.
	 *
	 * @param id the wicket:id
	 */
	public BootstrapDatepicker(String id) {
		super(id);
		add(new BootstrapDatePickerBehaviour() {
			private static final long serialVersionUID = 1L;
		
			@Override
			public Collection<SpecialDate> getSpecialDates() {
				return BootstrapDatepicker.this.getSpecialDates();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onConfigure()
	 */
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		dateField = getDateTextField();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if(dateField!=null){
			tag.put("data-date-format", dateField.getTextFormat().toLowerCase());
		}
	}

	/**
	 * Gets the DateTextField inside this datepicker wrapper.
	 *
	 * @return the date field
	 */
	public DateTextField getDateTextField(){
		DateTextField component = visitChildren(DateTextField.class, new IVisitor<DateTextField, DateTextField>() {
			@Override
			public void component(DateTextField arg0, IVisit<DateTextField> arg1) {
				arg1.stop(arg0);
			}
		});

		if (component == null)
			throw new WicketRuntimeException("BootstrapDatepicker didn't have any DateTextField child!");

		return component;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.IGenericComponent#getModel()
	 */
	@Override
	public IModel<Date> getModel() {
		return getDateTextField().getModel();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.IGenericComponent#setModel(org.apache.wicket.model.IModel)
	 */
	@Override
	public void setModel(IModel<Date> model) {
		getDateTextField().setModel(model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.IGenericComponent#setModelObject(java.lang.Object)
	 */
	@Override
	public void setModelObject(Date object) {
		getDateTextField().setModelObject(object);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.IGenericComponent#getModelObject()
	 */
	@Override
	public Date getModelObject() {
		return getDateTextField().getModelObject();
	}
	
	
	/**
	 * Gets the special dates for this datepicker
	 *
	 * @return a collection with the special dates
	 */
	public Collection<SpecialDate> getSpecialDates() {
		return null;
	}

}
