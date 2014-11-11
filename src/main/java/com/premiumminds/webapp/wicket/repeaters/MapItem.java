package com.premiumminds.webapp.wicket.repeaters;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.model.IModel;

public class MapItem<K, V> extends AbstractItem implements IGenericComponent<Map.Entry<K, V>> {
	private static final long serialVersionUID = 1613540034820483520L;

	public MapItem(String id, IModel<Map.Entry<K, V>> model) {
		super(id, model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModel<Entry<K, V>> getModel() {
		return (IModel<Entry<K, V>>) getDefaultModel();
	}

	@Override
	public void setModel(IModel<Entry<K, V>> model) {
		throw new UnsupportedOperationException("can't change model");
	}

	@Override
	public void setModelObject(Entry<K, V> object) {
		throw new UnsupportedOperationException("can't change model");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entry<K, V> getModelObject() {
		return (Entry<K, V>) getDefaultModelObject();
	}

}
