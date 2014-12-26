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

package com.premiumminds.webapp.wicket.drawer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;

/**
 * A wicket drawer manager that allows stackable modals and supports AJAX-based opening and closing.
 *
 */
public class DrawerManager extends Panel {
	private static final long serialVersionUID = -352740762144075341L;

	private static final ResourceReference DRAWER_JAVASCRIPT = new JQueryPluginResourceReference(DrawerManager.class, "bootstrap-modaldrawer.js");
	private static final ResourceReference MANAGER_JAVASCRIPT = new JQueryPluginResourceReference(DrawerManager.class, "bootstrap-modalmanager.js");
	private static final ResourceReference DRAWER_CSS = new CssResourceReference(DrawerManager.class, "bootstrap-modaldrawer.css");
	
	private static class ListItem extends Panel {
		private static final long serialVersionUID = 3805629765663003401L;

		private WebMarkupContainer item;
		private AbstractDrawer drawer;
		private ListItem next;
		private ListItem previous;
		private DrawerManager manager;

		public ListItem(String id, final AbstractDrawer drawer, DrawerManager drawerManager, String css) {
			super(id);
			setOutputMarkupId(true);

			manager = drawerManager;
			item = new WebMarkupContainer("item");
			if (null != css) {
				item.add(new AttributeAppender("class", Model.of(css), " "));
			}
			add(item);
			this.drawer = drawer;
			item.add(drawer);
			add(new EmptyPanel("next").setOutputMarkupId(true));

			item.add(new AjaxEventBehavior("hide-modal") {
				private static final long serialVersionUID = -6423164614673441582L;

				@Override
				protected void onEvent(AjaxRequestTarget target) {
					manager.eventPop(ListItem.this.drawer, target);
				}
			});
		}

		public void add(ListItem item) {
			next = item;
			addOrReplace(item);
			item.previous = this;
		}
	}

	private Deque<ListItem> drawers = new ArrayDeque<ListItem>();
	private ListItem first;

	public DrawerManager(String id) {
		super(id);
		add(new EmptyPanel("next").setOutputMarkupId(true));
	}

	/**
	 * Push and display a drawer on the page during page construction.
	 * 
	 * Do not call this method during an AJAX request.
	 * Use {@link #push(AbstractDrawer drawer, AjaxRequestTarget target)} instead.
	 * 
	 * @param drawer
	 * 			The drawer to be pushed. Cannot be null.
	 */
	public void push(AbstractDrawer drawer) {
		push(drawer, null, null);
	}

	/**
	 * Push and display a drawer on the page during an AJAX request.
	 * 
	 * @param drawer
	 * 			The drawer to be pushed. Cannot be null.
	 * @param target
	 * 			The current AJAX target.
	 */
	public void push(AbstractDrawer drawer, AjaxRequestTarget target) {
		push(drawer, target, null);
	}

	/**
	 * Push and display a drawer on the page during page construction,
	 * and inject an optional CSS class onto the drawer's immediate container.
	 * 
	 * Do not call this method during an AJAX request.
	 * Use {@link #push(AbstractDrawer drawer, AjaxRequestTarget target, String cssClass)} instead.
	 * 
	 * @param drawer
	 * 			The drawer to be pushed. Cannot be null.
	 * @param cssClass
	 * 			The name of the class to be injected.
	 */
	public void push(AbstractDrawer drawer, String cssClass) {
		push(drawer, null, cssClass);
	}

	/**
	 * Push and display a drawer on the page during an AJAX request,
	 * and inject an optional CSS class onto the drawer's immediate container.
	 * 
	 * @param drawer
	 * 			The drawer to be pushed. Cannot be null.
	 * @param target
	 * 			The current AJAX target.
	 * @param cssClass
	 * 			The name of the class to be injected.
	 */
	public void push(AbstractDrawer drawer, AjaxRequestTarget target, String cssClass) {
		ListItem item = new ListItem("next", drawer, this, cssClass);

		drawers.push(item);
		if (first == null) {
			first = item;
			addOrReplace(first);
		} else {
			ListItem iter = first;
			while (iter.next != null) {
				iter = iter.next;
			}
			iter.add(item);
		}

		if (target!=null) {
			target.add(item);

			target.appendJavaScript("$('#"+item.item.getMarkupId()+"').modaldrawer('show');");
			if (item.previous!=null) {
				target.appendJavaScript("$('#"+item.previous.item.getMarkupId()+"').removeClass('shown-modal');");
				target.appendJavaScript("$('#"+item.previous.item.getMarkupId()+"').addClass('hidden-modal');");
			}

		}
		drawer.setManager(this);
	}

	/**
	 * Pops a series of open drawers from the stack and closes them, displaying the one underneath, or the main page.
	 * This method requires an AJAX request. DrawerManager does not support closing drawers during page construction.
	 * Note that this code sets each drawer's allow close flag to true. See {@link AsbtractDrawer} for more information.
	 * 
	 * @param drawer
	 * 			The last drawer to be closed. This drawer and all the ones above it are closed.
	 * 			Open drawers beneath this one are not. If this parameter is null or not in the stack,
	 * 			all the drawers are closed.
	 * @param target
	 * 			The current AJAX target. Cannot be null.
	 * 
	 */
	public void pop(AbstractDrawer drawer, AjaxRequestTarget target) {
		Iterator<ListItem> iter = drawers.iterator();
		while (iter.hasNext()) {
			ListItem item = iter.next();
			item.drawer.setAllowClose(true);
			target.appendJavaScript("$('#"+item.item.getMarkupId()+"').modaldrawer('hide');");
			if (item.drawer==drawer) {
				break;
			}
		}
	}
	
	/**
	 * Replaces the topmost open drawer with a new one. If there is no open drawer, this method does nothing.
	 * This method requires an AJAX request. DrawerManager does not support swapping drawers during page construction.
	 * 
	 * @param newDrawer
	 * 			The new drawer to open. Cannot be null.
	 * @param target
	 * 			The current AJAX target. Cannot be null.
	 */
	public void replaceLast(AbstractDrawer newDrawer, AjaxRequestTarget target) {
		if (!drawers.isEmpty()) {
			ListItem last = drawers.getFirst();
			last.drawer.replaceWith(newDrawer);
			last.drawer = newDrawer;
			newDrawer.setManager(this);
			target.add(newDrawer);
		}
	}
	
	/**
	 * @param drawerClass
	 * 			The expected class of the topmost open drawer.
	 * @return
	 * 			A reference to the topmost open drawer, or null if there isn't one.
	 * 			If the topmost open drawer cannot be cast to the class in the first argument,
	 * 			a runtime class cast exception occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractDrawer> T getLast(Class<T> drawerClass) {
		if (drawers.isEmpty()) {
			return null;
		}
		ListItem last = drawers.getFirst();
		return (T) last.drawer;
	}
	
	/**
	 * @return
	 * 			The wicket page relative path of the topmost open drawer, or null
	 * 			if there isn't an open drawer.
	 */
	public String getLastItemRelativePath() {
		if (drawers.isEmpty()) {
			return null;
		}
		ListItem last = drawers.getFirst();
		return last.item.getPageRelativePath();
	}

	private void eventPop(AbstractDrawer drawer, AjaxRequestTarget target) {
		ListItem item = null;
		ListItem lastClosed = null;

		// It's impossible for eventPop to be called with a drawer argument that's not in the stack.
		// Thus, drawers.peek() is never null. - JMMM
		do {
			item = drawers.peek();

			item.drawer.beforeClose(target);
			if (!item.drawer.isAllowClose()) {
				break;
			}

			lastClosed = drawers.pop();
			internalPop(lastClosed, target);
		} while (item.drawer != drawer);

		if ((lastClosed != null) && (lastClosed.previous != null)) {
			target.appendJavaScript("$('#"+lastClosed.previous.item.getMarkupId()+"').addClass('shown-modal');");
			target.appendJavaScript("$('#"+lastClosed.previous.item.getMarkupId()+"').removeClass('hidden-modal');");
		}
	}

	private void internalPop(ListItem item, AjaxRequestTarget target) {
		target.appendJavaScript("$('#"+item.item.getMarkupId()+"').unbind('hide-modal');");
		target.appendJavaScript("$('#"+item.item.getMarkupId()+"').data('modal-drawer').isShown=true;");
		target.appendJavaScript("$('#"+item.item.getMarkupId()+"').modaldrawer('hide');");
		target.appendJavaScript("$('#"+item.item.getMarkupId()+"').removeClass('shown-modal');");

		if (item.previous == null) {
			first = null;
			removeDrawer(this, target);
		} else {
			item.previous.next = null;
			removeDrawer(item.previous, target);
		}
	}

	private void removeDrawer(MarkupContainer previous, AjaxRequestTarget target) {
		Panel panel = new EmptyPanel("next");
		panel.setOutputMarkupId(true);
		previous.addOrReplace(panel);
		target.add(panel);
	}

	//This script calls in this method are necessary to correctly respond to users
	//pressing the Back button and landing on a page with open drawers.
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(JavaScriptHeaderItem.forReference(DRAWER_JAVASCRIPT));
		response.render(JavaScriptHeaderItem.forReference(MANAGER_JAVASCRIPT));
		response.render(CssHeaderItem.forReference(DRAWER_CSS));

		Iterator<ListItem> iter = drawers.descendingIterator();
		WebMarkupContainer drawer;
		while (iter.hasNext()) {
			drawer=iter.next().item;
			response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').modaldrawer('show');"));
			if (drawers.getFirst().item.equals(drawer)) {
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').addClass('shown-modal');"));
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').removeClass('hidden-modal');"));
			} else {
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').removeClass('shown-modal');"));
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').addClass('hidden-modal');"));
			}
		}
	}
}
