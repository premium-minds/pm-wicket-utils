package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapFeedbackPanel;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.elements.ListControlGroups;

public class BootstrapStatelessCrudifierForm<T> extends StatelessForm<T> {
	private static final long serialVersionUID = -1762699420685191222L;

	public BootstrapStatelessCrudifierForm(String id, IModel<T> model, CrudifierSettings configuration) {
		super(id, model);

		setOutputMarkupId(true);
		
		add(new Label("legend", new StringResourceModel("legend", this, getModel(), "Unknown")));
		add(new BootstrapFeedbackPanel("feedbackError", FeedbackMessage.ERROR).setEscapeModelStrings(false));
		add(new BootstrapFeedbackPanel("feedbackWarning", FeedbackMessage.WARNING).setEscapeModelStrings(false));
		add(new BootstrapFeedbackPanel("feedbackSuccess", FeedbackMessage.SUCCESS).setEscapeModelStrings(false));
		
		add(new ListControlGroups<T>("controls", getModel(), configuration));
		
		add(new Label("submitLabel", new StringResourceModel("submitLabel", this, getModel(), "Submit")));
		add(new Label("resetLabel", new StringResourceModel("resetLabel", this, getModel(), "Reset")));
	}
	
	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new PanelMarkupSourcingStrategy(false);
	}

}
