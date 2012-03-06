package com.premiumminds.webapp.wicket.behaviours;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class DefaultFocusBehaviour extends Behavior {
	private static final long serialVersionUID = 7102614107259820127L;
	@Override
	public void bind(Component component) {
		super.bind(component);
		component.setOutputMarkupId(true);
	}
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		  super.renderHead(component, response);
		  response.renderOnLoadJavaScript("document.getElementById('" + component.getMarkupId() + "').focus();");
	}
}
