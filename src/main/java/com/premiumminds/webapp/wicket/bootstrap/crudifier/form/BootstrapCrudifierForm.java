package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
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
import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierEntitySettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.AbstractControlGroup;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ControlGroupProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ListControlGroups;

public class BootstrapCrudifierForm<T> extends Form<T> implements IBootstrapCrudifierForm<T> {
	private static final long serialVersionUID = -611772486341659737L;

	private ListControlGroups<T> listControlGroups;

	private CrudifierFormSettings formSettings;
	private CrudifierEntitySettings entitySettings;
	private Map<Class<?>, IObjectRenderer<?>> renderers;
	private WebMarkupContainer reset;	
	private ButtonType btnType;
	
	public enum ButtonType {DEFAULT
		, PRIMARY
		, SUCCESS
		, INFO
		, WARNING
		, DANGER
		};
	
	public BootstrapCrudifierForm(String id, IModel<T> model, CrudifierEntitySettings entitySettings, CrudifierFormSettings formSettings, Map<Class<?>, IObjectRenderer<?>> renderers, ButtonType submitButtonType){
		super(id, model);
		this.btnType = submitButtonType;
		
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
			private static final long serialVersionUID = 8621555399423058702L;

			@Override
			protected EntityProvider<?> getEntityProvider(String name) {
				return BootstrapCrudifierForm.this.getEntityProvider(name);
			}
			
		});
		
		add(new AjaxSubmitLink("submit", this) {
			private static final long serialVersionUID = -4527592607129929399L;

			{
				add(AttributeModifier.append("class", getCssClass()));
			}
			
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
		reset = new WebMarkupContainer("reset");
		add(reset);
		reset.add(new Label("resetLabel", new StringResourceModel("reset.label", this, getModel(), "Reset")));
	}
	
	public BootstrapCrudifierForm(String id, IModel<T> model){
		this(id, model, new CrudifierEntitySettings(), new CrudifierFormSettings(), new HashMap<Class<?>, IObjectRenderer<?>>());
	}
	
	public BootstrapCrudifierForm(String id, IModel<T> model, CrudifierEntitySettings entitySettings, CrudifierFormSettings formSettings, Map<Class<?>, IObjectRenderer<?>> renderers) {
		this(id, model, entitySettings, formSettings, renderers, ButtonType.PRIMARY);
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
	
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
	}
	
	protected void onError(AjaxRequestTarget target, Form<?> form) {
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
	
	/**
	 * get the providers used in this form. To add new providers, simply make a put() to map returned by this method
	 * @return
	 */
	public Map<Class<?>, ControlGroupProvider<? extends AbstractControlGroup<?>>> getControlGroupProviders(){
		return listControlGroups.getControlGroupProviders();
	}
	
	protected String getCssClass() {
		String rval = "btn ";
		switch(btnType) {
		case DANGER:
			rval += "btn-danger";
			break;
		case INFO:
			rval += "btn-info";
			break;
		case PRIMARY:
			rval += "btn-primary";
			break;
		case SUCCESS:
			rval += "btn-success";
			break;
		case WARNING:
			rval += "btn-warning";
			break;
		default:
			rval += "btn-default";
			break;
		}
		return rval;
	}
}
