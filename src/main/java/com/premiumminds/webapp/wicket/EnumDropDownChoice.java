package com.premiumminds.webapp.wicket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/*
 * Drop down select box for ENUMs
 */
public class EnumDropDownChoice<T extends Enum<T>> extends DropDownChoice<T> {
	private static final long serialVersionUID = -3880641372777323104L;

	public EnumDropDownChoice(String id, IModel<T> model, Class<? extends Enum<T>> enumType) {
		super(id);
		setModel(model);
		
		try {
			Method method;
			method = enumType.getMethod("values");
			@SuppressWarnings("unchecked")
			T[] values = (T[]) method.invoke(null);
			
			setChoices(Arrays.asList(values));
			setChoiceRenderer(new ChoiceEnumRender());
		} catch (SecurityException e) {
			throw new RuntimeException("Error getting the available values for enum "+enumType.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Error getting the available values for enum "+enumType.getName(), e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Error getting the available values for enum "+enumType.getName(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Error getting the available values for enum "+enumType.getName(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Error getting the available values for enum "+enumType.getName(), e);
		}
		
	}
	
	private class ChoiceEnumRender extends ChoiceRenderer<T> {
		private static final long serialVersionUID = 1211651886440891035L;
		@Override
		public Object getDisplayValue(T object) {
			return getString(object.name());
		}
		@Override
		public String getIdValue(T object, int index) {
			return object.name();
		}
	}

}
