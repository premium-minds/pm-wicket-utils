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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.joda.time.ReadableInstant;

import com.premiumminds.webapp.wicket.JodaInstantTextField;
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

	@Override
	protected void onConfigure() {
		super.onConfigure();

		dateField = visitChildren(JodaInstantTextField.class, new IVisitor<JodaInstantTextField<T>, JodaInstantTextField<T>>() {
			@Override
			public void component(JodaInstantTextField<T> arg0, IVisit<JodaInstantTextField<T>> arg1) {
				arg1.stop(arg0);
			}
		});
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
