package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapFeedbackPanel;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.AbstractControlGroup;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ListControlGroups;

public class BootstrapCrudifierForm<T> extends Form<T> implements IBootstrapCrudifierForm<T> {
	private static final long serialVersionUID = -611772486341659737L;

	private ListControlGroups<T> listControlGroups;
	
	public BootstrapCrudifierForm(String id, IModel<T> model, CrudifierSettings configuration) {
		super(id, model);

		setOutputMarkupId(true);
		
		add(new Label("legend", new StringResourceModel("legend", this, getModel(), "Unknown")){
			private static final long serialVersionUID = -7854751811138463187L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				setVisible(!getDefaultModelObjectAsString().isEmpty());
			}
		});
		
		if(configuration.isWithSelfFeedback()){
			add(new BootstrapFeedbackPanel("feedbackError", FeedbackMessage.ERROR).setEscapeModelStrings(false));
			add(new BootstrapFeedbackPanel("feedbackWarning", FeedbackMessage.WARNING).setEscapeModelStrings(false));
			add(new BootstrapFeedbackPanel("feedbackSuccess", FeedbackMessage.SUCCESS).setEscapeModelStrings(false));
		} else {
			add(new WebMarkupContainer("feedbackError").setVisible(false));
			add(new WebMarkupContainer("feedbackWarning").setVisible(false));
			add(new WebMarkupContainer("feedbackSuccess").setVisible(false));
		}
		
		add(listControlGroups = new ListControlGroups<T>("controls", getModel(), configuration));
		
		add(new AjaxSubmitLink("submit", this) {
			private static final long serialVersionUID = -4527592607129929399L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.add(BootstrapCrudifierForm.this);
				BootstrapCrudifierForm.this.onSubmit(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(BootstrapCrudifierForm.this);
				BootstrapCrudifierForm.this.onError(target, form);
			}
		}.add(new Label("submitLabel", new StringResourceModel("submit.label", this, getModel(), "Submit"))));
		WebMarkupContainer reset = new WebMarkupContainer("reset");
		add(reset);
		reset.setVisible(configuration.isShowReset());
		reset.add(new Label("resetLabel", new StringResourceModel("reset.label", this, getModel(), "Reset")));
	}
	
	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new PanelMarkupSourcingStrategy(false);
	}

	public FormComponent<?> getFormComponent(String propertyName) {
		Map<String, AbstractControlGroup<?>> fields = listControlGroups.getFieldsControlGroup();
		if(!fields.containsKey(propertyName)) throw new RuntimeException("No property "+propertyName+" was found on the form");
		return fields.get(propertyName).getFormComponent();
	}
	
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
	}
	
	protected void onError(AjaxRequestTarget target, Form<?> form) {
	}
	
}
