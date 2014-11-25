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
package com.premiumminds.webapp.wicket.repeaters;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.model.IModel;

/**
 * Container that holds components in a {@link AjaxListSetView}.
 * 
 * @see AjaxListSetView
 * @author acamilo
 *
 * @param <T> model object type
 */
public class ListSetItem<T> extends AbstractItem implements IGenericComponent<T> {
	private static final long serialVersionUID = 1993267843582407117L;

	/**
	 * @param id component id
	 * @param model model for this item
	 */
	public ListSetItem(String id, IModel<T> model) {
		super(id, model);
		setOutputMarkupPlaceholderTag(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}

	@Override
	public void setModel(IModel<T> model) {
		setDefaultModel(model);
	}

	@Override
	public void setModelObject(T object) {
		setDefaultModelObject(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getModelObject() {
		return (T) getDefaultModelObject();
	}

}
