/**
 * Copyright (C) 2016 Premium Minds.
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

import java.util.Date;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.premiumminds.webapp.wicket.bootstrap.datetimepicker.BootstrapDateTimePickerBehaviour;

/**
 * Bootstrap DateTime picker for Wicket.
 *
 * @author npires
 * @see <a href="https://eonasdan.github.io/bootstrap-datetimepicker/">Bootstrap 3 DateTime picker</a>
 * 
 * Requires jQuery, BootStrap and Font Awesome. Warning: make sure your jQuery is loaded as a priority header item!
 */
public class BootstrapDateTimePicker extends WebMarkupContainer implements IGenericComponent<Date> 
{
	private static final long serialVersionUID = 1L;
	
	private DateTextField dateField;
	
	/**
	 * Instantiates a new bootstrap datetimepicker.
	 *
	 * @param id the wicket:id
	 */
	public BootstrapDateTimePicker(String id)
	{
		super(id);

		add(new BootstrapDateTimePickerBehaviour());
	}

	@Override
	protected void onConfigure() 
	{
		super.onConfigure();
		
		dateField = getDateTextField();
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) 
	{
		super.onComponentTag(tag);
		
		if(dateField!=null) {
			
			tag.put("data-date-format", dateField.getTextFormat().toLowerCase());
		}
	}

	/**
	 * Gets the DateTextField inside this datepicker wrapper.
	 *
	 * @return the date field
	 */
	public DateTextField getDateTextField()
	{
		DateTextField component = visitChildren(DateTextField.class, new IVisitor<DateTextField, DateTextField>() {
			@Override
			public void component(DateTextField arg0, IVisit<DateTextField> arg1) {
				arg1.stop(arg0);
			}
		});

		if (component == null) {
			
			throw new WicketRuntimeException("BootstrapDateTimepicker didn't have any DateTextField child!");
		}
		
		return component;
	}

	@Override
	public IModel<Date> getModel() 
	{
		return getDateTextField().getModel();
	}

	@Override
	public void setModel(IModel<Date> model) 
	{
		getDateTextField().setModel(model);
	}

	@Override
	public void setModelObject(Date object) 
	{
		getDateTextField().setModelObject(object);
	}
	
	@Override
	public Date getModelObject() 
	{
		return getDateTextField().getModelObject();
	}
}
