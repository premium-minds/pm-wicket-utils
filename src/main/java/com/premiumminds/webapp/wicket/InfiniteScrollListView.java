package com.premiumminds.webapp.wicket;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public abstract class InfiniteScrollListView<T> extends WebMarkupContainer {
	private static final long serialVersionUID = 3906374470444741117L;
	
	private static final int MAX_ELEMENTS = 100;
	private static final int SCROLL_SIZE = 20;
	private static final ResourceReference javascriptReference = new JavaScriptResourceReference(InfiniteScrollListView.class, "InfiniteScrollListView.js");
	
	private WebMarkupContainer listContainer;
	private WebMarkupContainer upLoading;
	private WebMarkupContainer downLoading;
	
	private ListView<T> listView;
	
	private AbstractDefaultAjaxBehavior upBehavior;
	private AbstractDefaultAjaxBehavior downBehavior;

	public InfiniteScrollListView(String id, PageableListModel<? extends List<? extends T>> model) {
		super(id, model);
	}
	
	@SuppressWarnings("serial")
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		setOutputMarkupPlaceholderTag(true);
		
		add(listContainer = new WebMarkupContainer("list"));
		listContainer.setOutputMarkupPlaceholderTag(true);
		
		listContainer.add(upLoading = new WebMarkupContainer("upLoading"));
		upLoading.setOutputMarkupPlaceholderTag(true);
		upLoading.setVisible(false);
		
		add(upBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = -1895111598850513557L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				target.appendJavaScript("InfiniteScroll.getFromContainer('"+InfiniteScrollListView.this.getMarkupId()+"').scrollDownTo('"+getItemMarkupId(InfiniteScrollListView.this.getModel().getObject().get(0))+"')");
				
				setStartIndex(InfiniteScrollListView.this.getModel().getStartIndex()-SCROLL_SIZE);
				
				refreshLoadings();

				target.add(listContainer);
			}
		}); 
		
		listView = new ListView<T>("list", new LoadableDetachableModel<List<? extends T>>(){
				@Override
				protected List<? extends T> load() {
					return getModel().getObject();
				}
			})
		{
			private static final long serialVersionUID = -1218380580005295126L;

			@Override
			protected void populateItem(ListItem<T> item) {
				InfiniteScrollListView.this.populateItem(item);
				item.setMarkupId(getItemMarkupId(item.getModelObject()));
			}
			
			@Override
			public void renderHead(IHeaderResponse response) {
				String containerId = InfiniteScrollListView.this.getMarkupId();
				
				response.render(JavaScriptHeaderItem.forReference(javascriptReference, "infinite-scroll-list-view-js"));

				response.render(OnDomReadyHeaderItem.forScript("InfiniteScroll.getFromContainer('"+containerId+"').changeUp("+Boolean.toString(isShowUpLoading())+")"));
				response.render(OnDomReadyHeaderItem.forScript("InfiniteScroll.getFromContainer('"+containerId+"').changeDown("+Boolean.toString(isShowDownLoading())+")"));
				
				super.renderHead(response);
			}
		};
		listContainer.add(listView);
		
		listContainer.add(downLoading = new WebMarkupContainer("downLoading"));
		downLoading.setOutputMarkupPlaceholderTag(true);
		downLoading.setVisible(getModel().getSize()>MAX_ELEMENTS);
		
		add(downBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = -1895111598850513557L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				target.appendJavaScript("InfiniteScroll.getFromContainer('"+InfiniteScrollListView.this.getMarkupId()+"')" +
						".scrollUpTo('"+getItemMarkupId(InfiniteScrollListView.this.getModel().getObject().get(
								Math.min(InfiniteScrollListView.this.getModel().getViewSize()-1, InfiniteScrollListView.this.getModel().getObject().size()-1)
						  ))+"')");
				setStartIndex(getModel().getStartIndex()+SCROLL_SIZE);

				refreshLoadings();
				
				target.add(listContainer);
			}
		});
		
		add(getScrollBehaviour());
	}
	
	protected abstract void populateItem(final ListItem<T> item);
	
	protected abstract String getItemMarkupId(final T item);
	
	private Behavior getScrollBehaviour(){
		return new AttributeModifier("onscroll", Model.of(this.getMarkupId())){
			private static final long serialVersionUID = 3523727356782417598L;

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);

				response.render(OnDomReadyHeaderItem.forScript("InfiniteScroll.getFromContainer('"+getMarkupId()+"').setUrls('"+upBehavior.getCallbackUrl()+"', '"+downBehavior.getCallbackUrl()+"')"));
			}
			
			@Override
			protected String newValue(String currentValue, String replacementValue) {
				return "InfiniteScroll.handleScroll('"+InfiniteScrollListView.this.getMarkupId()+"')"; 
			}
		};
	}
	
	@Override
	protected void onBeforeRender() {
		setStartIndex(0);

		refreshLoadings();
		
		super.onBeforeRender();
	}
	
	private boolean isShowUpLoading(){
		return getModel().getStartIndex()>0;
	}
	
	private boolean isShowDownLoading(){
		return getModel().getSize()-getModel().getStartIndex()>MAX_ELEMENTS;
	}
	
	private void setStartIndex(int index){
		getModel().setStartIndex(index, MAX_ELEMENTS);
	}
	
	@SuppressWarnings("unchecked")
	public PageableListModel<? extends List<? extends T>> getModel(){
		return (PageableListModel<? extends List<? extends T>>) getDefaultModel();
	}

	public void setModel(PageableListModel<? extends List<? extends T>> model){
		setDefaultModel(model);
	}
	
	private void refreshLoadings(){
		upLoading.setVisible(isShowUpLoading());
		downLoading.setVisible(isShowDownLoading());
	}
}
