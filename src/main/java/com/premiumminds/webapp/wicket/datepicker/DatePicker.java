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
package com.premiumminds.webapp.wicket.datepicker;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class DatePicker extends Behavior {
	private static final long serialVersionUID = -2779776745316284795L;
	
	private static final ResourceReference DATE_PICKER_JS = new JavaScriptResourceReference(DatePicker.class, "Picker.js");
	private static final ResourceReference DATE_PICKER_ATTACH_JS = new JavaScriptResourceReference(DatePicker.class, "Picker.Attach.js");
	private static final ResourceReference DATE_PICKER_DATE_JS = new JavaScriptResourceReference(DatePicker.class, "Picker.Date.js");
	
	private static final ResourceReference DATE_PICKER_DATE_CSS = new CssResourceReference(DatePicker.class, "datepicker.css");

	private Component container;
	
	public DatePicker(Component container){
		this.container = container;
	}
	
	@Override
	public void bind(Component component) {
		component.setOutputMarkupId(true);
		getDatePattern();
	}
	
	protected String getDatePattern()
	{
//		String format = null;
//		if (dateField instanceof ITextFormatProvider)
//		{
//			format = ((ITextFormatProvider)dateField).getTextFormat();
//			// it is possible that components implement ITextFormatProvider but
//			// don't provide a format
//		} else {
//			throw new WicketRuntimeException("cant get date pattern of parent component");
//		}
		return "%d-%m-%Y";
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(CssReferenceHeaderItem.forReference(DATE_PICKER_DATE_CSS, "screen"));
		response.render(JavaScriptHeaderItem.forReference(DATE_PICKER_JS));
		response.render(JavaScriptHeaderItem.forReference(DATE_PICKER_ATTACH_JS));
		response.render(JavaScriptHeaderItem.forReference(DATE_PICKER_DATE_JS));
		
		if(component.isEnabledInHierarchy()){
			
			StringBuilder sb = new StringBuilder();
			sb.append("Date.defineParser(\"").append(getDatePattern()).append("\");");
			response.render(JavaScriptHeaderItem.forScript(sb.toString(), "pickerDatePattern"));
			
			sb = new StringBuilder();
			
			sb.append("new Picker.Date($(\"").append(component.getMarkupId()).append("\"), {");
			
			sb.append("format : \"").append(getDatePattern()).append("\",");
			sb.append("inject : $(\"").append(container.getMarkupId()).append("\"),");
	
			sb.append("months_abr : [");
			sb.append("\"Jan\",");
			sb.append("\"Fev\",");
			sb.append("\"Mar\",");
			sb.append("\"Abr\",");
			sb.append("\"Mai\",");
			sb.append("\"Jun\",");
			sb.append("\"Jul\",");
			sb.append("\"Ago\",");
			sb.append("\"Set\",");
			sb.append("\"Out\",");
			sb.append("\"Nov\",");
			sb.append("\"Dez\",");
			sb.append("],");
	
			sb.append("days_abr : [");
			sb.append("\"Seg\",");
			sb.append("\"Ter\",");
			sb.append("\"Qua\",");
			sb.append("\"Qui\",");
			sb.append("\"Sex\",");
			sb.append("\"Sab\",");
			sb.append("\"Dom\",");
			sb.append("],");
			
			sb.append("})");
			
			response.render(OnLoadHeaderItem.forScript(sb.toString()));
		}
	}

	
}
