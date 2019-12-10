/**
 * Copyright (C) 2016 Premium Minds.
 *
 * This file is part of pm-wicket-utils.
 *
 * pm-wicket-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-wicket-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-wicket-utils. If not, see <http://www.gnu.org/licenses/>.
 */
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
