package com.premiumminds.webapp.wicket.behaviours;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;

public class DisableSubmitOnEnterBehaviour extends Behavior {
	private static final long serialVersionUID = 8716392865358960423L;

	@Override
	public void bind(Component component) {
		super.bind(component);
		if(!(component instanceof Form<?>)) throw new WicketRuntimeException("can only add this component to forms");
	}
	
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		component.setOutputMarkupId(true);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(OnDomReadyHeaderItem.forScript("$(\"#"+component.getMarkupId()+"\").keypress(function(e){ return e.keyCode!=13; });"));
	}
}
