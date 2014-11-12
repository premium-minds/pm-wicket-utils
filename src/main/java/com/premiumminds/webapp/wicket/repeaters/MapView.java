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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class MapView<K extends Number,V extends Serializable> extends AbstractRepeater implements IGenericComponent<Map<K, V>> {
	private static final long serialVersionUID = -3771292136946484795L;
	
	private Map<K, MapItem<K, V>> mapComponents = new HashMap<K, MapItem<K, V>>();
	private List<MapItem<K, V>> components = new ArrayList<MapItem<K, V>>();
	
	private boolean insertionOrderInverted = false;

	public MapView(String id, IModel<? extends Map<K, V>> model) {
		super(id, model);
	}

	public MapView(String id, Map<K, V> map) {
		super(id, Model.ofMap(map));
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		boolean temp = insertionOrderInverted;
		insertionOrderInverted = false;
		populate();
		insertionOrderInverted = temp;
	}
	
	@Override
	protected Iterator<? extends Component> renderIterator() {
		return components.iterator();
	}

	@Override
	protected void onPopulate() {
		Map<K, V> mapFromModel = getModelObject();

		Iterator<MapItem<K, V>> iterator = components.iterator();
		while(iterator.hasNext()){
			MapItem<K, V> component = iterator.next();
			if(!mapFromModel.containsKey(component.getKey())){
				remove(component);
				iterator.remove();
				mapComponents.remove(component.getKey());
			}
		}
		
		for(Map.Entry<K, V> entry : mapFromModel.entrySet()){
			if(!mapComponents.containsKey(entry.getKey())){
				MapItem<K, V> newItem = newItem(entry.getKey(), Model.of(entry.getValue()));
				mapComponents.put(entry.getKey(), newItem);
				if(insertionOrderInverted) components.add(0, newItem);
				else components.add(newItem);
				add(newItem);
			}
		}
	}

	protected MapItem<K, V> newItem(K key, IModel<V> itemModel)
	{
		MapItem<K, V> abstractItem = new MapItem<K, V>(key, itemModel);
		populateItem(abstractItem);
		return abstractItem;
	}

	protected void onBeginPopulateItem(MapItem<K, V> item)
	{
	}
	
	protected abstract void populateItem(MapItem<K, V> item);
	
	public void populate(){
		onPopulate();
	}
	
	public MapItem<K, V> getMapComponent(K key){
		return mapComponents.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModel<Map<K, V>> getModel() {
		return (IModel<Map<K, V>>) getDefaultModel();
	}

	@Override
	public void setModel(IModel<Map<K, V>> model) {
		setDefaultModel(model);
	}

	@Override
	public void setModelObject(Map<K, V> object) {
		setDefaultModelObject(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<K, V> getModelObject() {
		return (Map<K, V>) getDefaultModelObject();
	}

	public boolean isInsertionOrderInverted() {
		return insertionOrderInverted;
	}

	public void setInsertionOrderInverted(boolean insertionOrderInverted) {
		this.insertionOrderInverted = insertionOrderInverted;
	}
}
