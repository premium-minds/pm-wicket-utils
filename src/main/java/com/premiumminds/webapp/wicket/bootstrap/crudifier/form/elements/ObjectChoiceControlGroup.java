package com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.IValidationError;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;

public class ObjectChoiceControlGroup<T> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = -8444849747715611613L;

	private DropDownChoice<T> dropDown;
	private EntityProvider<T> entityProvider;
	private IObjectRenderer<T> renderer;
	
	@SuppressWarnings("serial")
	public ObjectChoiceControlGroup(String id, IModel<T> model) {
		super(id, model);
		
		IModel<List<? extends T>> modelList = new LoadableDetachableModel<List<? extends T>>() {
			private static final long serialVersionUID = -3995535290067544541L;

			@Override
			protected List<T> load() {
				if(entityProvider==null) throw new RuntimeException("no entity provider for '"+getPropertyName()+"'");
				return (List<T>) entityProvider.load();
			}
			
			
		};

		dropDown = new DropDownChoice<T>("input", getModel(), modelList){
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
		
		dropDown.setChoiceRenderer(new ChoiceRenderer());
		WebMarkupContainer inputBox = new WebMarkupContainer("inputBox");
		addInputBoxGridSize(inputBox);
		inputBox.add(dropDown);
		add(new BootstrapControlGroupFeedback("controlGroup").add(inputBox));
	}

	@Override
	public FormComponent<T> getFormComponent() {
		return dropDown;
	}
	
	@SuppressWarnings("unchecked")
	public void setConfiguration(EntityProvider<?> entityProvider, IObjectRenderer<?> renderer){
		this.entityProvider = (EntityProvider<T>) entityProvider;
		this.renderer = (IObjectRenderer<T>) renderer;
	}
	
	private class ChoiceRenderer implements IChoiceRenderer<T> {
		private static final long serialVersionUID = -584810566291563698L;

		public Object getDisplayValue(T obj) {
			return renderer.render(obj);
		}

		public String getIdValue(T obj, int index) {
			//TODO retrieve @Id value if present
			return Integer.toString(index);
		}
		
	}
}
