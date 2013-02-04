package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;

public class EnumControlGroup<T extends Enum<?>> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = -7800336998276030740L;

	private RadioGroup<T> radioGroup;
	
	public EnumControlGroup(String id, IModel<T> model) {
		super(id, model);

		radioGroup = new RadioGroup<T>("radioGroup", getModel());
	}

	@Override
	public FormComponent<T> getFormComponent() {
		return radioGroup;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		try {
			Method method = getType().getMethod("values");
			@SuppressWarnings("unchecked")
			T[] values = (T[]) method.invoke(null);
			
			RepeatingView view = new RepeatingView("repeating");
			for(T value : values){
				Radio<T> radio = new Radio<T>("input", Model.of(value), radioGroup){
					private static final long serialVersionUID = 8903955236018583915L;

					@Override
					public String getValue() {
						return getModel().getObject().name();
					}
					
					@Override
					protected boolean getStatelessHint() {
						return true;
					}
				};
				radio.add(new Label("label", new StringResourceModel(getPropertyName()+"."+value.name(), getResourceBase(), getModel(), value.name())));
				
				view.add(new WebMarkupContainer(view.newChildId()).add(radio));
			}
			
			radioGroup.add(view);
			
			add(new BootstrapControlGroupFeedback("controlGroup")
				.add(radioGroup)
				.add(new Label("label", new StringResourceModel(getPropertyName()+".label", getResourceBase(), getModel(), getPropertyName())))
			);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
