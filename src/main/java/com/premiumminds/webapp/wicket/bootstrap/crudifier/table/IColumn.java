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

import java.util.Map;

import org.apache.wicket.Component;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;

public interface IColumn<T> {
	public String getPropertyName();
	public Component createComponent(String id, T object, Component resourceBase, Map<Class<?>, IObjectRenderer<?>> renderers); 
	public ColumnAlign getAlign();
}
