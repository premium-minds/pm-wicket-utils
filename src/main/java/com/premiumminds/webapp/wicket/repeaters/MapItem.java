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

public class MapItem<K extends Number, V> extends AbstractItem implements IGenericComponent<V> {
	private static final long serialVersionUID = 1613540034820483520L;

	private K key;
	
	public MapItem(K key, IModel<V> model) {
		super(String.valueOf(key), model);
		this.key = key;
	}

	public K getKey(){
		return key;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IModel<V> getModel() {
		return (IModel<V>) getDefaultModel();
	}

	@Override
	public void setModel(IModel<V> model) {
		throw new UnsupportedOperationException("can't change model");
	}

	@Override
	public void setModelObject(V object) {
		throw new UnsupportedOperationException("can't change model");
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getModelObject() {
		return (V) getDefaultModelObject();
	}

}
