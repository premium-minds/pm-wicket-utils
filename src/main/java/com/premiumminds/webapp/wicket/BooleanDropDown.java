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

import java.util.Arrays;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * Shows a drop-down selection for Boolean values. If the model contains a
 * starting null, the null value is shown. If the model contains a true or false
 * value, the null value is hidden. Can be configured to be a required field, or
 * to accept resetting the value back to null.
 * 
 * @author joao.neves@premium-minds.com
 */
@SuppressWarnings("serial")
public class BooleanDropDown extends DropDownChoice<Boolean> {
	public BooleanDropDown(String id, IModel<Boolean> model) {
		this(id, model, false, false);
	}

	/**
	 * @param id
	 *            - The wicket component identifier
	 * @param model
	 *            - The model from where to obtain the data
	 * @param required
	 *            - Causes a validation error on submittion if the value is null
	 *            (default = false)
	 */
	public BooleanDropDown(String id, IModel<Boolean> model, boolean required) {
		this(id, model, required, false);
	}

	/**
	 * @param id
	 *            - The wicket component identifier
	 * @param model
	 *            - The model from where to obtain the data
	 * @param required
	 *            - Causes a validation error on submittion if the value is null
	 *            (default = false)
	 * 
	 * @param canRevertToNull
	 *            - Allows setting the value back to null after the model has
	 *            been set to true or false (default = false)
	 */
	public BooleanDropDown(String id, IModel<Boolean> model, boolean required, boolean canRevertToNull) {
		super(id);

		setModel(model);

		if (required) {
			setRequired(true);
		} else {
			// null can only be valid if the value is not required 
			setNullValid(canRevertToNull);
		}

		setChoices(Arrays.asList(Boolean.TRUE, Boolean.FALSE));
		setChoiceRenderer(new BooleanWithNullRenderer());
	}

	private class BooleanWithNullRenderer extends ChoiceRenderer<Boolean> {
		@Override
		public Object getDisplayValue(Boolean object) {
			return getString(String.valueOf(object));
		}

		@Override
		public String getIdValue(Boolean object, int index) {
			return String.valueOf(object);
		}
	}

	//Needed if setNullValid was called. Without this the null label is blank.
	@Override
	protected String getNullValidKey() {
		return "null";
	}
}
