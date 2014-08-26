/**
 * Copyright (C) 2014 Premium Minds.
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
package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.IValidationError;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;

@SuppressWarnings("rawtypes")
public class CollectionControlGroup<T> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = 2580977991384659320L;

	private ListMultipleChoice multiChoice;
	
	@SuppressWarnings({ "unchecked", "serial" })
	public CollectionControlGroup(String id, IModel<T> model) {
		super(id, model);
		
		IModel<List<T>> modelList = new LoadableDetachableModel<List<T>>() {
			private static final long serialVersionUID = 3674039468142186197L;

			@Override
			protected List<T> load() {
				EntityProvider<?> provider = getConfiguration().getProviders().get(getPropertyName());
				if(provider==null) throw new RuntimeException("Provider for property "+getPropertyName()+" is missing, check your "+CrudifierSettings.class.getSimpleName());
				return (List<T>) getConfiguration().getProviders().get(getPropertyName()).load();
			}
		};
		
		multiChoice = new ListMultipleChoice("input", getModel(), modelList){
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
		
		add(new BootstrapControlGroupFeedback("controlGroup").add(multiChoice));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormComponent<T> getFormComponent() {
		return multiChoice;
	}

}
