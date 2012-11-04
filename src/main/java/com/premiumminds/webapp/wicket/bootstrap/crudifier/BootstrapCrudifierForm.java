package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapFeedbackPanel;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.elements.ListControlGroups;

public class BootstrapCrudifierForm<T> extends Form<T> {
	private static final long serialVersionUID = -611772486341659737L;

	public BootstrapCrudifierForm(String id, IModel<T> model, CrudifierSettings configuration) {
		super(id, model);

		setOutputMarkupId(true);
		
		add(new Label("legend", new StringResourceModel("legend", this, getModel(), "Unknown")));
		add(new BootstrapFeedbackPanel("feedbackError", FeedbackMessage.ERROR).setEscapeModelStrings(false));
		add(new BootstrapFeedbackPanel("feedbackWarning", FeedbackMessage.WARNING).setEscapeModelStrings(false));
		add(new BootstrapFeedbackPanel("feedbackSuccess", FeedbackMessage.SUCCESS).setEscapeModelStrings(false));
		
		add(new ListControlGroups<T>("controls", getModel(), configuration));
		
		add(new AjaxSubmitLink("submit", this) {
			private static final long serialVersionUID = -4527592607129929399L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.add(BootstrapCrudifierForm.this);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(BootstrapCrudifierForm.this);
			}
		}.add(new Label("submitLabel", new StringResourceModel("submitLabel", this, getModel(), "Submit"))));
		add(new Label("resetLabel", new StringResourceModel("resetLabel", this, getModel(), "Reset")));
		
	}
	
	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new PanelMarkupSourcingStrategy(false);
	}
}
