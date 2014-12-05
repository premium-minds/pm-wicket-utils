package com.premiumminds.webapp.wicket.drawer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class DrawerManager extends Panel {
	private static final long serialVersionUID = -352740762144075341L; 

	private Deque<ListItem> drawers = new ArrayDeque<ListItem>();
	private ListItem first;

	public DrawerManager(String id) {
		super(id);
		add(new EmptyPanel("next").setOutputMarkupId(true));
	}

	public void push(AbstractDrawer drawer){
		push(drawer, null);
	}

	public void push(AbstractDrawer drawer, AjaxRequestTarget target, String cssClass){
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

		if(target!=null){
			target.add(item);

			target.appendJavaScript("$('#"+item.item.getMarkupId()+"').modaldrawer('show');");
			if(item.previous!=null){
				target.appendJavaScript("$('#"+item.previous.item.getMarkupId()+"').removeClass('shown-modal');");
				target.appendJavaScript("$('#"+item.previous.item.getMarkupId()+"').addClass('hidden-modal');");
			}

		}
		drawer.setManager(this);
	}
	
	public void push(AbstractDrawer drawer, AjaxRequestTarget target){
		push(drawer, target, null);
	}

	public void pop(AbstractDrawer drawer, AjaxRequestTarget target){
		Iterator<ListItem> iter = drawers.iterator();
		while(iter.hasNext()){
			ListItem item = iter.next();
			item.drawer.setAllowClose(true);
			target.appendJavaScript("$('#"+item.item.getMarkupId()+"').modaldrawer('hide');");
			if(item.drawer==drawer){
				break;
			}
		}
	}
	
	public void replaceLast(AbstractDrawer newDrawer, AjaxRequestTarget target){
		if(!drawers.isEmpty()){
			ListItem last = drawers.getFirst();
			last.drawer.replaceWith(newDrawer);
			last.drawer = newDrawer;
			newDrawer.setManager(this);
			target.add(newDrawer);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractDrawer> T getLast(Class<T> drawerClass){
		if(drawers.isEmpty()) {
			return null;
		}
		ListItem last = drawers.getFirst();
		return (T) last.drawer;
	}
	
	public String getLastItemRelativePath(){
		if(drawers.isEmpty()) {
			return null;
		}
		ListItem last = drawers.getFirst();
		return last.item.getPageRelativePath();
	}

	private void eventPop(AbstractDrawer drawer, AjaxRequestTarget target){
		ListItem item = drawers.pop();
		while(item.drawer!=drawer){
			internalPop(item, target);
			item = drawers.pop();
		}
		internalPop(item, target);
	}

	private void internalPop(ListItem item, AjaxRequestTarget target){
		target.appendJavaScript("$('#"+item.item.getMarkupId()+"').removeClass('shown-modal');");
		if(item.previous!=null){
			target.appendJavaScript("$('#"+item.previous.item.getMarkupId()+"').addClass('shown-modal');");
			target.appendJavaScript("$('#"+item.previous.item.getMarkupId()+"').removeClass('hidden-modal');");
		}

		if(item.previous==null){
			first = null;
			removeDrawer(this, target);
		} else {
			item.previous.next = null;
			removeDrawer(item.previous, target);
		}
	}

	private void removeDrawer(MarkupContainer previous, AjaxRequestTarget target){
		Panel panel = new EmptyPanel("next");
		panel.setOutputMarkupId(true);
		previous.addOrReplace(panel);
		target.add(panel);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		Iterator<ListItem> iter = drawers.descendingIterator();
		WebMarkupContainer drawer;
		while(iter.hasNext()){
			drawer=iter.next().item;
			response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').modaldrawer('show');"));
			if(drawers.getFirst().item.equals(drawer)){
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').addClass('shown-modal');"));
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').removeClass('hidden-modal');"));
			} else {
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').removeClass('shown-modal');"));
				response.render(OnDomReadyHeaderItem.forScript("$('#"+drawer.getMarkupId()+"').addClass('hidden-modal');"));
			}
		}
	}

	public static class ListItem extends Panel {
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
			if(null != css){
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
					ListItem.this.drawer.onClose(target);
					if(ListItem.this.drawer.isAllowClose()){
						manager.eventPop(ListItem.this.drawer, target);
						target.appendJavaScript("$('#"+item.getMarkupId()+"').unbind('hide-modal');");
						target.appendJavaScript("$('#"+item.getMarkupId()+"').data('modal-drawer').isShown=true;");
						target.appendJavaScript("$('#"+item.getMarkupId()+"').modaldrawer('hide');");
					}
				}
			});
		}

		public void add(ListItem item){
			next = item;
			addOrReplace(item);
			item.previous = this;
		}
	}
}