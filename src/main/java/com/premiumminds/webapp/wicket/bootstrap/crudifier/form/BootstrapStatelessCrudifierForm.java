package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapFeedbackPanel;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierEntitySettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.AbstractControlGroup;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ListControlGroups;

public class BootstrapStatelessCrudifierForm<T> extends StatelessForm<T> implements IBootstrapCrudifierForm<T> {
	private static final long serialVersionUID = -1762699420685191222L;

	private ListControlGroups<T> listControlGroups;
	private WebMarkupContainer reset;
	
	private CrudifierFormSettings formSettings;
	private CrudifierEntitySettings entitySettings;
	private Map<Class<?>, IObjectRenderer<?>> renderers;
	
	public BootstrapStatelessCrudifierForm(String id, IModel<T> model, CrudifierEntitySettings entitySettings, CrudifierFormSettings formSettings, Map<Class<?>, IObjectRenderer<?>> renderers) {
		super(id, model);

		setOutputMarkupId(true);
		
		this.formSettings = formSettings;
		this.entitySettings = entitySettings;
		this.renderers = renderers;
		
		add(new Label("legend", new StringResourceModel("legend", this, getModel(), "Unknown")){
			private static final long serialVersionUID = -7854751811138463187L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				setVisible(!getDefaultModelObjectAsString().isEmpty());
			}
		});

		add(listControlGroups = new ListControlGroups<T>("controls", getModel(), entitySettings, renderers){
			private static final long serialVersionUID = 2855610861985645116L;

			@Override
			protected EntityProvider<?> getEntityProvider(String name) {
				return getEntityProvider(name);
			}
		});
		
		add(new Label("submitLabel", new StringResourceModel("submitLabel", this, getModel(), "Submit")));
		reset = new WebMarkupContainer("reset");
		add(reset);
		reset.add(new Label("resetLabel", new StringResourceModel("resetLabel", this, getModel(), "Reset")));
	}

	public BootstrapStatelessCrudifierForm(String id, IModel<T> model) {
		this(id, model, new CrudifierEntitySettings(), new CrudifierFormSettings(), new HashMap<Class<?>, IObjectRenderer<?>>());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		if(formSettings.isWithSelfFeedback()){
			add(new BootstrapFeedbackPanel("feedbackError", FeedbackMessage.ERROR).setEscapeModelStrings(false));
			add(new BootstrapFeedbackPanel("feedbackWarning", FeedbackMessage.WARNING).setEscapeModelStrings(false));
			add(new BootstrapFeedbackPanel("feedbackSuccess", FeedbackMessage.SUCCESS).setEscapeModelStrings(false));
		} else {
			add(new WebMarkupContainer("feedbackError").setVisible(false));
			add(new WebMarkupContainer("feedbackWarning").setVisible(false));
			add(new WebMarkupContainer("feedbackSuccess").setVisible(false));
		}
		reset.setVisible(formSettings.isShowReset());
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
	
	protected EntityProvider<?> getEntityProvider(String name){
		throw new RuntimeException("provider not found for '"+name+"', please override getEntityProvider");
	}
	
	public CrudifierFormSettings getFormSettings() {
		return formSettings;
	}

	public CrudifierEntitySettings getEntitySettings() {
		return entitySettings;
	}

	public Map<Class<?>, IObjectRenderer<?>> getRenderers() {
		return renderers;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<Class<?>, Class<? extends AbstractControlGroup>> getControlGroupsTypesMap(){
		return listControlGroups.getControlGroupsTypesMap();
	}
	
}
