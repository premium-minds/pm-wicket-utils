package com.premiumminds.webapp.wicket.bootstrap.datepicker;

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

public class BootstrapDatePickerBehaviour extends Behavior {
	private static final long serialVersionUID = 6150624915791893034L;

	private static final ResourceReference DATE_PICKER_CSS = new CssResourceReference(BootstrapDatePickerBehaviour.class, "datepicker.css");
	
	private static final ResourceReference DATE_PICKER_JAVASCRIPT = new JavaScriptResourceReference(BootstrapDatePickerBehaviour.class, "bootstrap-datepicker.js");
	
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		
		component.setOutputMarkupId(true);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		if(component.isEnabledInHierarchy()){
			response.render(CssReferenceHeaderItem.forReference(DATE_PICKER_CSS));
			response.render(JavaScriptHeaderItem.forReference(DATE_PICKER_JAVASCRIPT));
			
			if(!component.getLocale().getLanguage().equals("en")){
				response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BootstrapDatePickerBehaviour.class, "locales/bootstrap-datepicker."+component.getLocale().getLanguage()+".js")));
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
}
