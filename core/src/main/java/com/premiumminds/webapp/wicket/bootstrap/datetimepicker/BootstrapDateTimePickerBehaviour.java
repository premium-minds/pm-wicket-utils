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

package com.premiumminds.webapp.wicket.bootstrap.datetimepicker;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class BootstrapDateTimePickerBehaviour extends Behavior 
{
	private static final long serialVersionUID = 1L;
	
	private static final ResourceReference DATETIME_PICKER_CSS 			= new CssResourceReference(BootstrapDateTimePickerBehaviour.class, "bootstrap-datetimepicker.min.css");
	private static final ResourceReference DATETIME_PICKER_JAVASCRIPT 	= new JavaScriptResourceReference(BootstrapDateTimePickerBehaviour.class, "bootstrap-datetimepicker.js");
	private static final ResourceReference MOMENT_JAVASCRIPT 			= new JavaScriptResourceReference(BootstrapDateTimePickerBehaviour.class, "moment-with-locales.min.js");

	@Override
	public void onConfigure(Component component) 
	{
		super.onConfigure(component);
		
		component.setOutputMarkupId(true);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) 
	{
		super.renderHead(component, response);
		
		if(component.isEnabledInHierarchy()) {
			
			response.render(CssReferenceHeaderItem.forReference(DATETIME_PICKER_CSS));
			response.render(JavaScriptHeaderItem.forReference(MOMENT_JAVASCRIPT));
			response.render(JavaScriptHeaderItem.forReference(DATETIME_PICKER_JAVASCRIPT));
			
			if(!component.getLocale().getLanguage().equals("en")) {
				
				response.render(JavaScriptHeaderItem.forReference(
									new JavaScriptResourceReference(BootstrapDateTimePickerBehaviour.class
																  , "locales/" + component.getLocale().getLanguage() + ".js")));
			}
			
			/** Not supporting special dates at the moment **/
			
			response.render(OnDomReadyHeaderItem.forScript("$(\"#" + component.getMarkupId() + "\").datetimepicker({ "
																										+ " format: 'DD/MM/YYYY HH:mm:ss',"
																										+ " icons: { "
																											+ " time: 'fa fa-clock-o',"
																											+ " date: 'fa fa-calendar',"
																											+ " up: 'fa fa-arrow-up',"
																											+ " down: 'fa fa-arrow-down',"
																											+ " previous: 'fa fa-arrow-left',"
																											+ " next: 'fa fa-arrow-right'"
																										+ "}"
																									+ "});"
					
			));
		}
	}
	
	@Override
	public void onComponentTag(Component component, ComponentTag tag) 
	{
		super.onComponentTag(component, tag);
		
		if(component.isEnabledInHierarchy()) {
			
			tag.put("data-date-language", component.getLocale().getLanguage());
		}
	}
}
