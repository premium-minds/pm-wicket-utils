package com.premiumminds.webapp.wicket.behaviours;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;

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
		  response.render(OnLoadHeaderItem.forScript("document.getElementById('" + component.getMarkupId() + "').focus();"));
	}
}
