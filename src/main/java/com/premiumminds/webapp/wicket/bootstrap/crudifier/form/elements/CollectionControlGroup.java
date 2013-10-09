package com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.IValidationError;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;

@SuppressWarnings("rawtypes")
public class CollectionControlGroup<T> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = 2580977991384659320L;

	private ListMultipleChoice multiChoice;
	private EntityProvider<T> entityProvider;
	private Map<Class<?>, IObjectRenderer<?>> renderers;
	
	@SuppressWarnings({ "unchecked", "serial" })
	public CollectionControlGroup(String id, IModel<T> model) {
		super(id, model);
		
		IModel<List<T>> modelList = new LoadableDetachableModel<List<T>>() {
			private static final long serialVersionUID = 3674039468142186197L;

			@Override
			protected List<T> load() {
				return (List<T>) entityProvider.load();
			}
		};
		
		multiChoice = new ListMultipleChoice("input", getModel(), modelList, new ChoiceRenderer()){
			@Override
			public void error(IValidationError error) {
				MessageSource source = new MessageSource();
				Serializable message = error.getErrorMessage(source);
				
				super.error(message);
			}
		};
	}


	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		WebMarkupContainer inputBox = new WebMarkupContainer("inputBox");
		addInputBoxGridSize(inputBox);
		inputBox.add(multiChoice);
		add(new BootstrapControlGroupFeedback("controlGroup").add(inputBox));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormComponent<T> getFormComponent() {
		return multiChoice;
	}
	
	@SuppressWarnings("unchecked")
	public void setConfiguration(EntityProvider<?> entityProvider, Map<Class<?>, IObjectRenderer<?>> renderers){
		this.entityProvider = (EntityProvider<T>) entityProvider;
		this.renderers = renderers;
	}

	private class ChoiceRenderer implements IChoiceRenderer<T> {
		private static final long serialVersionUID = -584810566291563698L;

		public Object getDisplayValue(T obj) {
			@SuppressWarnings("unchecked")
			IObjectRenderer<T> renderer = (IObjectRenderer<T>) renderers.get(obj.getClass());
			if(renderer==null) return obj.toString(); 
			return renderer.render(obj);
		}

		public String getIdValue(T obj, int index) {
			//TODO retrieve @Id value if present
			return Integer.toString(index);
		}
		
	}	
}
