package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.AbstractControlGroup;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements.ControlGroupProvider;

public interface ICrudifierForm<T> {
	public FormComponent<?> getFormComponent(String propertyName);

	public CrudifierFormSettings getFormSettings();

	public CrudifierEntitySettings getEntitySettings();

	public Map<Class<?>, IObjectRenderer<?>> getRenderers();

	@SuppressWarnings("rawtypes")
	public Map<Class<?>, Class<? extends AbstractControlGroup>> getControlGroupsTypesMap();

	public Map<Class<?>, ControlGroupProvider<? extends AbstractControlGroup<?>>> getControlGroupProviders();

	/**
	 * Get the list of custom buttons for the form. This buttons will appear
	 * next to submit button. All buttons must have their wicket:id="button" and
	 * must have a label inside them with wicket:id="label"
	 * <p>
	 * Usage:
	 * 
	 * <pre>
	 * 
	 * 	form.getButtons().add(new AjaxLink<Void>(&quot;button&quot;) {
	 * 		public void onClick(AjaxRequestTarget target) {
	 * 			// do something
	 * 		}
	 * 	}.add(new Label(&quot;label&quot;, "Cancel")));
	 * 
	 * </pre>
	 * 
	 * @return the custom buttons list
	 */
	public List<Component> getButtons();
}
