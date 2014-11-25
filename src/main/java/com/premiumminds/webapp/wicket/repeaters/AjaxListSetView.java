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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * An AjaxListSetView is a repeater that makes it easy to display/work with {@link ListOrderedSet}s (collection with unique values, but with {@link List} order). 
 * This repeater is great when you have {@link FormComponent}s for each row and want to change the list through AJAX, because only new elements of the list are
 * created in the DOM, the rest is just reordered.
 * <p>
 * Example:
 * 
 * <pre>
 * &lt;table&gt;
 *   &lt;tr wicket:id=&quot;rows&quot; class=&quot;even&quot;&gt;
 *     &lt;td&gt;&lt;span wicket:id=&quot;id&quot;&gt;Test ID&lt;/span&gt;&lt;/td&gt;
 *     ...
 * </pre>
 * 
 * <p>
 * The related Java code:
 * 
 * <pre>
 * add(new AjaxListSetView&lt;UserDetails&gt;(&quot;rows&quot;, listModel, &quot;tbody&quot;)
 * {
 * 		public void populateItem(final ListSetItem&lt;UserDetails&gt; item)
 * 		{
 * 			item.add(new Label(&quot;id&quot;, new PropertyModel(item.getModel, &quot;id&quot;)));
 * 		}
 * });
 * </pre>
 * 
 * <p>
 * <strong>NOTE:</strong> AjaxListSetView creates a container between his items and the markups parent. In the example above, AjaxListSetView will 
 * insert the element tbody between table and tr.
 * 
 * <p>
 * 
 * @author acamilo
 *
 * @param <T> type of elements contained in the model's list
 */
public abstract class AjaxListSetView<T extends Serializable> extends AbstractRepeater2 {
	private static final long serialVersionUID = -2741997575388196264L;

	/** counter to generate rows index */
	private int counter = 1;

	/** cache for the items already created */
	private Map<T, ListSetItem<T>> elementToComponent = new HashMap<T, ListSetItem<T>>();
	
	/** markup tag used to create new elements through ajax */
	private String markupTag = null;
	
	/**
	 * @param id component id
	 * @param model model containing the listOrderedSet of
	 * @param bodyTag tag name for the container that will be created
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AjaxListSetView(String id, IModel<? extends ListOrderedSet<? extends T>> model, final String bodyTag) {
		super(id, model);
		
		if (model == null){
			throw new IllegalArgumentException("Null models are not allowed");
		}		
		
		add(new Behavior() {
			private static final long serialVersionUID = -2448715969183335401L;

			@Override
			public void beforeRender(Component component) {
				getResponse().write("<"+bodyTag+" id='"+component.getMarkupId(true)+"'>");
			}
			
			@Override
			public void afterRender(Component component) {
				getResponse().write("</"+bodyTag+">");
			}
		});
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		markupTag = ((ComponentTag) getMarkup().get(0)).getName();
	}
	
	@Override
	protected Iterator<? extends Component> renderIterator() {
		return IteratorUtils.transformedIterator(getList().iterator(), new Transformer<T, ListSetItem<?>>() {

			@Override
			public ListSetItem<?> transform(T el) {
				ListSetItem<?> component = elementToComponent.get(el);
				if(component==null) throw new IllegalStateException("could not find element '"+el+"' on computed map");
				return component;
			}
		});
	}
	
	private void updateItems(Set<ListSetItem<T>> newItems, Set<ListSetItem<T>> removedItems){
		Set<T> componentsNotHandled = new HashSet<T>(elementToComponent.keySet());
		
		for(T el : getList()){
			if(componentsNotHandled.contains(el)){
				componentsNotHandled.remove(el);
			} else {
				ListSetItem<T> item = newItem(Model.of(el));
				elementToComponent.put(el, item);
				newItems.add(item);
				add(item);
			}
		}

		for(T el : componentsNotHandled){
			removedItems.add(elementToComponent.get(el));
			remove(elementToComponent.get(el));
			elementToComponent.remove(el);
		}
	}

	@Override
	protected void onPopulate() {
		Set<ListSetItem<T>> newItems = new HashSet<ListSetItem<T>>();
		Set<ListSetItem<T>> removedItems = new HashSet<ListSetItem<T>>();

		updateItems(newItems, removedItems);
	}

	/**
	 * Create a new ListSetItem for list item.
	 * 
	 * @param itemModel object in the list that the item represents
	 * @return ListSetItem
	 */
	protected ListSetItem<T> newItem(IModel<T> itemModel)
	{
		ListSetItem<T> item = new ListSetItem<T>(Integer.toString(counter++), itemModel);
		populateItem(item);
		return item;
	}

	/**
	 * Populate a given item.
	 * <p>
	 * <b>be careful</b> to add any components to the list item. So, don't do:
	 * 
	 * <pre>
	 * add(new Label(&quot;foo&quot;, &quot;bar&quot;));
	 * </pre>
	 * 
	 * but:
	 * 
	 * <pre>
	 * item.add(new Label(&quot;foo&quot;, &quot;bar&quot;));
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param item
	 *            The item to populate
	 */
	protected abstract void populateItem(ListSetItem<T> item);
	
	@SuppressWarnings("unchecked")
	private ListOrderedSet<T> getList(){
		return (ListOrderedSet<T>) getDefaultModelObject();
	}
	
	private void updateAjax(AjaxRequestTarget target){
		Set<ListSetItem<T>> newItems = new HashSet<ListSetItem<T>>();
		Set<ListSetItem<T>> removedItems = new HashSet<ListSetItem<T>>();

		updateItems(newItems, removedItems);

		for(ListSetItem<T> item : removedItems){
			target.prependJavaScript("$('#"+item.getMarkupId()+"').remove();");
		}
		for(ListSetItem<T> item : newItems){
			String script = String.format(
                    "var item = document.createElement('%s'); " +
                    "item.id = '%s'; "+
                    "var container = Wicket.$('%s'); " + 
                    "$(container).append(item); "
                    , markupTag, item.getMarkupId(), getMarkupId());			
			target.prependJavaScript(script);
			target.add(item);
		}
		
		for(T el : getList()){
			ListSetItem<T> item = elementToComponent.get(el);
			target.appendJavaScript("$('#"+item.getMarkupId()+"').appendTo('#"+getMarkupId()+"');");
		}
	}
	
	/**
	 * FOR {@link AjaxListSetView} INTERNAL USE
	 * 
	 * @author acamilo
	 */
	public static class AjaxListener implements IListener {

		@Override
		public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
			Set<String> toRemove = new HashSet<String>();
			for(Map.Entry<String, Component> entry : map.entrySet()){
				if(entry.getValue() instanceof AjaxListSetView){
					AjaxListSetView<?> listView = (AjaxListSetView<?>) entry.getValue();
					listView.updateAjax(target);
					toRemove.add(entry.getKey());
				}
			}
		
			for(String key : toRemove){
				map.remove(key);
			}
		}

		@Override
		public void onAfterRespond(Map<String, Component> map, IJavaScriptResponse response) {
		}
		
	}
	
}
