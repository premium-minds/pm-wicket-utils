package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;
import com.premiumminds.webapp.wicket.validators.HibernateValidatorProperty;

public class ListControlGroups<T> extends Panel {
	private static final long serialVersionUID = 7205285700113097720L;
	
	private Map<String, AbstractControlGroup<?>> fieldComponents = new HashMap<String, AbstractControlGroup<?>>();
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class<?>, Class<? extends AbstractControlGroup>> typesControlGroups = new HashMap<Class<?>, Class<? extends AbstractControlGroup>>();
	static {
		typesControlGroups.put(Date.class, DateControlGroup.class);
		typesControlGroups.put(String.class, TextFieldControlGroup.class);
		typesControlGroups.put(Integer.class, TextFieldControlGroup.class);
		typesControlGroups.put(int.class, TextFieldControlGroup.class);
		typesControlGroups.put(Long.class, TextFieldControlGroup.class);
		typesControlGroups.put(long.class, TextFieldControlGroup.class);
		typesControlGroups.put(Boolean.class, CheckboxControlGroup.class);
		typesControlGroups.put(boolean.class, CheckboxControlGroup.class);
		typesControlGroups.put(Set.class, CollectionControlGroup.class);
		typesControlGroups.put(Double.class, TextFieldControlGroup.class);
		typesControlGroups.put(double.class, TextFieldControlGroup.class);
	}
	
	private List<ObjectProperties> objectProperties;
	private CrudifierSettings settings;
	
	public ListControlGroups(String id, IModel<T> model, CrudifierSettings settings) {
		super(id, model);
		
		objectProperties = new ArrayList<ObjectProperties>();
		this.settings = settings;
		
		Class<?> modelClass = model.getObject().getClass();
		
		Set<String> properties = getPropertiesByOrder(modelClass);
		
		Validator validator = HibernateValidatorProperty.validatorFactory.getValidator();
		BeanDescriptor constraintDescriptors = validator.getConstraintsForClass(model.getObject().getClass());
		for(String property : properties){
			PropertyDescriptor descriptor;
			try {
				descriptor = PropertyUtils.getPropertyDescriptor(model.getObject(), property);
			} catch (Exception e) {
				throw new RuntimeException("error getting property "+property, e);
			}
			
			boolean required = false;
			
			ElementDescriptor constraintDescriptor = constraintDescriptors.getConstraintsForProperty(descriptor.getName());
			if(constraintDescriptor!=null){
				Set<ConstraintDescriptor<?>> constraintsSet = constraintDescriptor.getConstraintDescriptors();
				for(ConstraintDescriptor<?> constraint : constraintsSet){
					if(constraint.getAnnotation() instanceof NotNull ||
					   constraint.getAnnotation() instanceof NotEmpty ||
					   constraint.getAnnotation() instanceof NotBlank)
						required = true;
				}
			}
			
			objectProperties.add(new ObjectProperties(descriptor, required));
		}
	}
	
	private Set<String> getPropertiesByOrder(Class<?> modelClass) {
		Set<String> properties = new LinkedHashSet<String>();
		
		for(String property : settings.getOrderOfFields()){
			if(!settings.getHiddenFields().contains(property))
				properties.add(property);
		}
		for(PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(modelClass)){
			if(!settings.getHiddenFields().contains(descriptor.getName()) &&
			   !properties.contains(descriptor.getName()) &&
			   !descriptor.getName().equals("class"))
				properties.add(descriptor.getName());
		}
		
		return properties;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		RepeatingView view = new RepeatingView("controlGroup");
		for(ObjectProperties objectProperty : objectProperties){
			Constructor<?> constructor;
			try {
				Class<? extends Panel> typesControlGroup = getControlGroupByType(objectProperty.type);
				if(typesControlGroup==null){
					if(objectProperty.type.isEnum()) typesControlGroup = EnumControlGroup.class;
					else if(settings.getProviders().containsKey(objectProperty.name)){
						typesControlGroup = ObjectChoiceControlGroup.class;
					} else {
						throw new RuntimeException("Crudifier didn't found a provider for property "+objectProperty.name+", check your "+CrudifierSettings.class.getSimpleName());
					}
				}
				
				constructor = typesControlGroup.getConstructor(String.class, IModel.class);

				AbstractControlGroup<?> controlGroup = (AbstractControlGroup<?>) constructor.newInstance(view.newChildId(), new PropertyModel<Object>(ListControlGroups.this.getModel(), objectProperty.name));
				controlGroup.init(objectProperty.name, getResourceBase(), settings, objectProperty.required, objectProperty.type);
				controlGroup.setEnabled(objectProperty.enabled);
				
				view.add(controlGroup);
				
				fieldComponents.put(objectProperty.name, controlGroup);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		
		add(view);
	}
	
	@SuppressWarnings("unchecked")
	public IModel<T> getModel(){
		return (IModel<T>) getDefaultModel();
	}
	
	public Component getResourceBase(){
		return this;
	}
	
	public Map<String, AbstractControlGroup<?>> getFieldsControlGroup(){
		return Collections.unmodifiableMap(fieldComponents);
	}
	
	@SuppressWarnings("rawtypes")
	private Class<? extends AbstractControlGroup> getControlGroupByType(Class<?> type){
		for(Class<?> mapType : typesControlGroups.keySet()){
			if(type.isAssignableFrom(mapType)) return typesControlGroups.get(mapType);
		}
		return null;
	}

	private static final class ObjectProperties implements Serializable {
		private static final long serialVersionUID = 1747577998897955928L;
		private String name;
		private boolean enabled;
		private Class<?> type;
		private boolean required;
		
		public ObjectProperties(PropertyDescriptor descriptor, boolean required){
			this.name = descriptor.getName();
			this.enabled = descriptor.getWriteMethod()!=null;
			this.type = descriptor.getPropertyType();
			this.required = required;
		}
	}
}
