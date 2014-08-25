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
package com.premiumminds.webapp.wicket.bootstrap.datepicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
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

import com.premiumminds.webapp.wicket.bootstrap.SpecialDate;
public class BootstrapDatePickerBehaviour extends Behavior {
	private static final long serialVersionUID = 6150624915791893034L;

	private static final ResourceReference DATE_PICKER_CSS = new CssResourceReference(BootstrapDatePickerBehaviour.class, "datepicker.css");
	
	private static final ResourceReference DATE_PICKER_JAVASCRIPT = new JavaScriptResourceReference(BootstrapDatePickerBehaviour.class, "bootstrap-datepicker.js");
	private static final ResourceReference DATE_PICKER_EXTENSION_JAVASCRIPT = new JavaScriptResourceReference(BootstrapDatePickerBehaviour.class, "bootstrap-datepicker-extension.js");
	
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		
		component.setOutputMarkupId(true);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		Collection<SpecialDate> specialDates = getSpecialDates();

		if(component.isEnabledInHierarchy()){
			response.render(CssReferenceHeaderItem.forReference(DATE_PICKER_CSS));
			response.render(JavaScriptHeaderItem.forReference(DATE_PICKER_JAVASCRIPT));
			response.render(JavaScriptHeaderItem.forReference(DATE_PICKER_EXTENSION_JAVASCRIPT));
			if(!component.getLocale().getLanguage().equals("en")){
				response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BootstrapDatePickerBehaviour.class, "locales/bootstrap-datepicker."+component.getLocale().getLanguage()+".js")));
			}

			if(null != specialDates && !specialDates.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				SpecialDate[] sdArray = specialDates.toArray(new SpecialDate[20]);
				for(int i = 0; i < specialDates.size(); ++i) {
					SpecialDate sd = sdArray[i];
					sb.append("{dt:new Date('"+df.format(sd.getDt())+"'), css:'"+sd.getCssClass()+"', tooltip:'"+ sd.getTooltip() +"'}");
					if(i < specialDates.size() - 1) {
						sb.append(",");
					}
				}
				sb.append("]");

				response.render(OnDomReadyHeaderItem.forScript("$(\"#"+component.getMarkupId()+"\").datepicker(null, "+sb.toString()+")"));
			} else {
				response.render(OnDomReadyHeaderItem.forScript("$(\"#"+component.getMarkupId()+"\").datepicker()"));
			}
		}
	}
	
	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		
		if(component.isEnabledInHierarchy()){
			tag.put("data-date-language", component.getLocale().getLanguage());
		}
	}
	
	public Collection<SpecialDate> getSpecialDates() {
		return null;
	}
}
