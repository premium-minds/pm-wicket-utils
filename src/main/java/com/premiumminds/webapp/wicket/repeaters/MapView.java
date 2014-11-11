package com.premiumminds.webapp.wicket.repeaters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class MapView<K extends Number,V extends Serializable> extends AbstractRepeater implements IGenericComponent<Map<K, V>> {
	private static final long serialVersionUID = -3771292136946484795L;
	
	private Map<K, Component> components = new HashMap<K, Component>();

	public MapView(String id, IModel<? extends Map<K, V>> model) {
		super(id, model);
	}

	public MapView(String id, Map<K, V> map) {
		super(id, Model.ofMap(map));
	}
	
	@Override
	protected Iterator<? extends Component> renderIterator() {
		return components.values().iterator();
	}

	@Override
	protected void onPopulate() {
		Map<K, V> mapFromModel = getModelObject();

		Iterator<Map.Entry<K, Component>> iterator = components.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<K, Component> entry = iterator.next();
			if(!mapFromModel.containsKey(entry.getKey())){
				remove(entry.getValue());
				iterator.remove();
			}
		}
		
		for(Map.Entry<K, V> entry : mapFromModel.entrySet()){
			if(!components.containsKey(entry.getKey())){
				AbstractItem newItem = newItem(new ReadOnlyModel(entry));
				components.put(entry.getKey(), newItem);
				add(newItem);
			}
		}
	}

	protected MapItem<K, V> newItem(IModel<Map.Entry<K, V>> itemModel)
	{
		MapItem<K, V> abstractItem = new MapItem<K, V>(itemModel.getObject().getKey().toString(), itemModel);
		populateItem(abstractItem);
		return abstractItem;
	}

	protected void onBeginPopulateItem(MapItem<K, V> item)
	{
	}
	
	protected abstract void populateItem(MapItem<K, V> item);

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
	
	private class ReadOnlyModel extends AbstractReadOnlyModel<Map.Entry<K,V>> implements Serializable {
		private static final long serialVersionUID = -2856441578148609149L;
		
		private Pair<K,V> entry;
		
		public ReadOnlyModel(Map.Entry<K,V> entry) {
			this.entry = Pair.of(entry.getKey(), entry.getValue());
		}

		@Override
		public Entry<K, V> getObject() {
			return new Map.Entry<K, V>() {

				@Override
				public K getKey() {
					return entry.getLeft();
				}

				@Override
				public V getValue() {
					return entry.getRight();
				}

				@Override
				public V setValue(V value) {
					throw new UnsupportedOperationException("can't call setValue");
				}
			};
		}
	}
}
