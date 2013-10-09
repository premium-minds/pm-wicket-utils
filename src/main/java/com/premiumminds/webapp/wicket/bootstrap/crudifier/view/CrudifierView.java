package com.premiumminds.webapp.wicket.bootstrap.crudifier.view;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierEntitySettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;

public class CrudifierView<T> extends Panel implements IGenericComponent<T> {
	private static final long serialVersionUID = -151637566983702881L;
	
	private CrudifierEntitySettings entitySettings = new CrudifierEntitySettings();
	private Map<Class<?>, IObjectRenderer<?>> renderers = new HashMap<Class<?>, IObjectRenderer<?>>();

	public CrudifierView(String id, IModel<T> model) {
		super(id, model);
		
		add(new Label("legend", new StringResourceModel("legend", this, getModel(), "Unknown")){
			private static final long serialVersionUID = -7854751811138463187L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				setVisible(!getDefaultModelObjectAsString().isEmpty());
			}
		});
		
		

	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		RepeatingView view = new RepeatingView("control");
		for(final String property : getPropertiesByOrder(getModelObject().getClass())){
			WebMarkupContainer control = new WebMarkupContainer(view.newChildId());
			view.add(control);
			
			control.add(new Label("label", new StringResourceModel("controls."+property+".label", this, getModel(), property)));
			control.add(new LabelProperty("input", new PropertyModel<Object>(getModel(), property), renderers) {
				private static final long serialVersionUID = 8561120757563362569L;

				@Override
				protected String getResourceString(String key, String defaultValue) {
					return getLocalizer().getStringIgnoreSettings("controls."+property+"."+key, CrudifierView.this, null, defaultValue);
				}
			});
		}
		add(view);
		
	}

	private Set<String> getPropertiesByOrder(Class<?> modelClass) {
		Set<String> properties = new LinkedHashSet<String>();
		
		for(String property : entitySettings.getOrderOfFields()){
			if(!entitySettings.getHiddenFields().contains(property))
				properties.add(property);
		}
		for(PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(modelClass)){
			if(!entitySettings.getHiddenFields().contains(descriptor.getName()) &&
			   !properties.contains(descriptor.getName()) &&
			   !descriptor.getName().equals("class"))
				properties.add(descriptor.getName());
		}
		
		return properties;
	}


	@SuppressWarnings("unchecked")
	public IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}

	public void setModel(IModel<T> model) {
		setDefaultModel(model);
	}

	public void setModelObject(T object) {
		setModelObject(object);
	}

	public T getModelObject() {
		return getModel().getObject();
	}

	public CrudifierEntitySettings getEntitySettings() {
		return entitySettings;
	}

	public void setEntitySettings(CrudifierEntitySettings entitySettings) {
		this.entitySettings = entitySettings;
	}

	public Map<Class<?>, IObjectRenderer<?>> getRenderers() {
		return renderers;
	}

	public void setRenderers(Map<Class<?>, IObjectRenderer<?>> renderers) {
		this.renderers = renderers;
	}
	
	
}
