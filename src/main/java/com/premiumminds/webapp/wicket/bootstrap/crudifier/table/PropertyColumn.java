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
package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import java.io.Serializable;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.LabelProperty;

public class PropertyColumn<T> implements IColumn<T>, Serializable {
	private static final long serialVersionUID = -1539532027947493672L;

	private String property;
	private ColumnAlign align;
	
	public PropertyColumn(String property){
		this(property, ColumnAlign.LEFT);
	}
	
	public PropertyColumn(String property, ColumnAlign align){
		this.property = property;
		this.align = align;
	}

	public IModel<Object> getPropertyModel(T object) {
		return new PropertyModel<Object>(object, property);
	}

	public ColumnAlign getAlign() {
		return align;
	}

	public String getPropertyName() {
		return property;
	}

	public Component createComponent(String id, T object, final Component resourceBase, Map<Class<?>, IObjectRenderer<?>> renderers) {
		return new LabelProperty(id, new PropertyModel<T>(object, property), renderers) {
			private static final long serialVersionUID = 8252695892385959442L;

			@Override
			protected String getResourceString(String key, String defaultValue) {
				return getLocalizer().getStringIgnoreSettings(property+"."+key, resourceBase, null, defaultValue);
			}
		};
	}

}
