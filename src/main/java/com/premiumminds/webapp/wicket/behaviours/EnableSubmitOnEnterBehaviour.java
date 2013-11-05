package com.premiumminds.webapp.wicket.behaviours;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class EnableSubmitOnEnterBehaviour extends Behavior {
	private static final long serialVersionUID = -7194468587182980219L;

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
		
		Form<?> form = (Form<?>) component;
		Component submitter = (Component) findSubmitButton(form);
		
		response.render(OnDomReadyHeaderItem.forScript("$(\"#"+component.getMarkupId()+"\").keypress(function(e){ if(e.keyCode==13) $(\"#"+submitter.getMarkupId()+"\").click(); });"));
	}
	
	private static IFormSubmitter findSubmitButton(final Form<?> form)
	{
		IFormSubmittingComponent submittingComponent = form.visitChildren(
			IFormSubmittingComponent.class, new IVisitor<Component, IFormSubmittingComponent>()
			{
				public void component(final Component component,
					final IVisit<IFormSubmittingComponent> visit)
				{
					// Get submitting component
					final IFormSubmittingComponent submittingComponent = (IFormSubmittingComponent)component;
					final Form<?> formFromComponent = submittingComponent.getForm();

					if ((formFromComponent != null) && (formFromComponent == form))
					{
						visit.stop(submittingComponent);
					}
				}
			});

		return submittingComponent;
	}

}
