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

	public BootstrapDatePickerBehaviour() {
	}
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

			if(null != specialDates && !specialDates.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append("var specialDates = [");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				SpecialDate[] sdArray = specialDates.toArray(new SpecialDate[20]);
				for(int i = 0; i < specialDates.size(); ++i) {
					SpecialDate sd = sdArray[i];
					sb.append("{dt:new Date('"+df.format(sd.getDt())+"'), css:'"+sd.getCssClass()+"', tooltip:'"+ sd.getTooltip() +"'}");
					if(i < specialDates.size() - 1) {
						sb.append(",");
					}
				}
				sb.append("];");
				sb.append("function isSpecialDate(_date) {for (var i=0, item; item = specialDates[i]; i++) {if(_date.getTime() == item.dt.getTime()) {return item;}}return false;};");
				response.render(JavaScriptHeaderItem.forScript(sb.toString(), "special-dates"));

				if(!component.getLocale().getLanguage().equals("en")){
					response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BootstrapDatePickerBehaviour.class, "locales/bootstrap-datepicker."+component.getLocale().getLanguage()+".js")));
				}
			}
			response.render(OnDomReadyHeaderItem.forScript("$(\"#"+component.getMarkupId()+"\").datepicker()"));
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
