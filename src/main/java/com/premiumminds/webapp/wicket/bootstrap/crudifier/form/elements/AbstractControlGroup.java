package com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.string.interpolator.VariableInterpolator;
import org.apache.wicket.validation.IErrorMessageSource;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierEntitySettings;
import com.premiumminds.webapp.wicket.validators.HibernateValidatorProperty;

public abstract class AbstractControlGroup<T> extends Panel {
	private static final long serialVersionUID = 6551088390404506493L;

	private String propertyName;
	private Component resourceBase;
	private Class<?> type;
	private boolean required;
	private CrudifierEntitySettings entitySettings;

	public AbstractControlGroup(String id, IModel<T> model) {
		super(id, model);
	}
	
	public void init(String propertyName, Component resourceBase, boolean required, Class<?> type, CrudifierEntitySettings entitySettings){
		this.propertyName = propertyName;
		this.resourceBase = resourceBase;
		this.type = type;
		this.required = required;
		this.entitySettings = entitySettings;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		getFormComponent().add(new HibernateValidatorProperty(getResourceBase().getDefaultModel(), getPropertyName()));

		StringResourceModel labelModel = new StringResourceModel(getPropertyName()+".label", resourceBase, getModel(), getPropertyName());
		getFormComponent().setLabel(labelModel);
		getFormComponent().setRequired(required);
	}
	
	public abstract FormComponent<T> getFormComponent();

	@SuppressWarnings("unchecked")
	public IModel<T> getModel(){
		return (IModel<T>) getDefaultModel();
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Component getResourceBase(){
		return resourceBase;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public CrudifierEntitySettings getEntitySettings(){
		return entitySettings;
	}

	protected void addInputBoxGridSize(WebMarkupContainer inputBox){
		String css = "col-lg-10"; // default
		if(getEntitySettings().getGridFieldsSizes().containsKey(getPropertyName())){
			switch(getEntitySettings().getGridFieldsSizes().get(getPropertyName())){
			case COL1:
				css = "col-lg-1";
				break;
			case COL2:
				css = "col-lg-2";
				break;
			case COL3:
				css = "col-lg-3";
				break;
			case COL4:
				css = "col-lg-4";
				break;
			case COL5:
				css = "col-lg-5";
				break;
			case COL6:
				css = "col-lg-6";
				break;
			case COL7:
				css = "col-lg-7";
				break;
			case COL8:
				css = "col-lg-8";
				break;
			case COL9:
				css = "col-lg-9";
				break;
			default:
				break;
			}
		}
		inputBox.add(AttributeModifier.append("class", css));
	}
	
	/**
	 * Copiado do FormComponent.MessageSource
	 * 
	 * {@link IErrorMessageSource} used for error messages against this form components.
	 * 
	 * @author ivaynberg
	 */
	protected class MessageSource implements IErrorMessageSource
	{
		private final Set<String> triedKeys = new LinkedHashSet<String>();

		/**
		 * @see org.apache.wicket.validation.IErrorMessageSource#getMessage(String, java.util.Map)
		 */
		public String getMessage(String key, Map<String, Object> vars)
		{
			final AbstractControlGroup<T> formComponent = AbstractControlGroup.this;

			// Use the following log4j config for detailed logging on the property resolution
			// process
			// log4j.logger.org.apache.wicket.resource.loader=DEBUG
			// log4j.logger.org.apache.wicket.Localizer=DEBUG

			final Localizer localizer = formComponent.getLocalizer();

			// retrieve prefix that will be used to construct message keys
			String prefix = getFormComponent().getValidatorKeyPrefix();
			String message = null;

			// first try the full form of key [form-component-id].[prefix].[key]
			String resource = getPropertyName() + "." + prefix(prefix, key);
			message = getString(localizer, resource, resourceBase);

			// if not found, try a more general form (without prefix)
			// [form-component-id].[key]
			if (Strings.isEmpty(message) && Strings.isEmpty(prefix))
			{
				resource = getPropertyName() + "." + key;
				message = getString(localizer, resource, resourceBase);
			}

			// If not found try a more general form [prefix].[key]
			if (Strings.isEmpty(message))
			{
				resource = prefix(prefix, key);
				message = getString(localizer, resource, formComponent);
			}

			// If not found try the most general form [key]
			if (Strings.isEmpty(message))
			{
				// Try a variation of the resource key
				message = getString(localizer, key, formComponent);
			}

			// convert empty string to null in case our default value of "" was
			// returned from localizer
			if (Strings.isEmpty(message))
			{
				message = null;
			}
			else
			{
				message = substitute(message, addDefaultVars(vars));
			}
			return message;
		}

		private String prefix(String prefix, String key)
		{
			if (!Strings.isEmpty(prefix))
			{
				return prefix + "." + key;
			}
			else
			{
				return key;
			}
		}

		/**
		 * 
		 * @param localizer
		 * @param key
		 * @param component
		 * @return string
		 */
		private String getString(Localizer localizer, String key, Component component)
		{
			triedKeys.add(key);

			// Note: It is important that the default value of "" is
			// provided to getString() not to throw a MissingResourceException or to
			// return a default string like "[Warning: String ..."
			return localizer.getString(key, component, "");
		}

		private String substitute(String string, final Map<String, Object> vars)
			throws IllegalStateException
		{
			return new VariableInterpolator(string, Application.get()
				.getResourceSettings()
				.getThrowExceptionOnMissingResource())
			{
				private static final long serialVersionUID = 1L;

				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				protected String getValue(String variableName)
				{
					Object value = vars.get(variableName);
					if (value == null)
					{
						return null;
					}
					else
					{
						IConverter converter = getConverter(value.getClass());
						if (converter == null)
						{
							return Strings.toString(value);
						}
						else
						{
							return converter.convertToString(value, getLocale());
						}
					}
				}
			}.toString();
		}

		/**
		 * Creates a new params map that additionally contains the default input, name, label
		 * parameters
		 * 
		 * @param params
		 *            original params map
		 * @return new params map
		 */
		private Map<String, Object> addDefaultVars(Map<String, Object> params)
		{
			// create and fill the new params map
			final HashMap<String, Object> fullParams;
			if (params == null)
			{
				fullParams = new HashMap<String, Object>(6);
			}
			else
			{
				fullParams = new HashMap<String, Object>(params.size() + 6);
				fullParams.putAll(params);
			}

			// add the input param if not already present
			if (!fullParams.containsKey("input"))
			{
				fullParams.put("input", getFormComponent().getInput());
			}

			// add the name param if not already present
			if (!fullParams.containsKey("name"))
			{
				fullParams.put("name", getPropertyName());
			}

			// add the label param if not already present
			if (!fullParams.containsKey("label"))
			{
				fullParams.put("label", getLabel());
			}
			return fullParams;
		}

		/**
		 * @return value of label param for this form component
		 */
		private String getLabel()
		{
			String label = null;

			// first try the label model ...
			if (getFormComponent().getLabel() != null)
			{
				label = getFormComponent().getLabel().getObject();
			}
			// ... then try a resource of format [form-component-id] with
			// default of '[form-component-id]'
			if (label == null)
			{

				label = getFormComponent().getDefaultLabel();
			}
			return label;
		}
	}
}
