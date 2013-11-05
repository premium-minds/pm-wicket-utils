package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapFeedbackPanel;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.AbstractControlGroup;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ControlGroupProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ListControlGroups;

/**
 * @author acamilo
 *
 * @param <T>
 */
public class CrudifierForm<T> extends Form<T> implements ICrudifierForm<T> {
	private static final long serialVersionUID = -611772486341659737L;

	private ListControlGroups<T> listControlGroups;

	private CrudifierFormSettings formSettings;
	private CrudifierEntitySettings entitySettings;
	private Map<Class<?>, IObjectRenderer<?>> renderers;
	
	private List<Component> buttons = new ArrayList<Component>();
	
	public CrudifierForm(String id, IModel<T> model, CrudifierEntitySettings entitySettings, CrudifierFormSettings formSettings, Map<Class<?>, IObjectRenderer<?>> renderers) {
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
			private static final long serialVersionUID = 8621555399423058702L;

			@Override
			protected EntityProvider<?> getEntityProvider(String name) {
				return CrudifierForm.this.getEntityProvider(name);
			}
			
		});
		
		add(new AjaxSubmitLink("submit", this) {
			private static final long serialVersionUID = -4527592607129929399L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.add(CrudifierForm.this);
				CrudifierForm.this.onSubmit(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(CrudifierForm.this);
				CrudifierForm.this.onError(target, form);
			}
		}.add(new Label("submitLabel", new StringResourceModel("submit.label", this, getModel(), "Submit"))));
		
		add(new ListView<Component>("buttons", buttons) {
			private static final long serialVersionUID = -8614436913101248043L;

			@Override
			protected void populateItem(ListItem<Component> item) {
				if(getApplication().usesDevelopmentConfig()){
					if(!"button".equals(item.getModelObject().getId())){
						throw new WicketRuntimeException("custom buttons must have the wicket:id=\"button\" and must have a label with wicket:id=\"label\"");
					}
					if(item.getModelObject().get("label")==null){
						throw new WicketRuntimeException("custom buttons must have a label inside them with wicket:id=\"label\"");
					}
				}
				item.add(item.getModelObject());
			}
		});
	}
	
	public CrudifierForm(String id, IModel<T> model){
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
	
	
	public List<Component> getButtons(){
		return buttons;
	}
}
