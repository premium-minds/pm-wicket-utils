package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;

public abstract class CrudifierTable<T> extends Panel {
	private static final long serialVersionUID = -6553624504699750680L;
	private static final ResourceReference CSS = new CssResourceReference(CrudifierTable.class, "CrudifierTable.css");
	
	private List<IColumn<T>> columns = new ArrayList<IColumn<T>>();
	private RepeatingView headers = new RepeatingView("headers");
	
	private boolean clickable = false;
	
	private ListView<T> listView;


	public CrudifierTable(String id, final CrudifierSettings settings) {
		super(id);
		
		add(headers);

		IModel<List<T>> modelList = new LoadableDetachableModel<List<T>>() {
			private static final long serialVersionUID = 3296835301481871094L;

			@Override
			protected List<T> load() {
				return CrudifierTable.this.load(0, 0);
			}
		};
		
		add(listView = new ListView<T>("list", modelList) {
			private static final long serialVersionUID = -2293426877086666745L;

			@Override
			protected void populateItem(final ListItem<T> item) {
				RepeatingView columns = new RepeatingView("columns");
				for(final IColumn<T> column : CrudifierTable.this.columns){
					columns.add(column.createComponent(columns.newChildId(), item.getModelObject(), CrudifierTable.this, settings));
				}
				
				item.add(columns);
				
				if(isClickable()){
					item.add(new AjaxEventBehavior("click") {
						private static final long serialVersionUID = -4889154387600763576L;

						@Override
						protected void onEvent(AjaxRequestTarget target) {
							onSelected(target, item.getModel());
						}
					});
					item.add(AttributeModifier.append("class", "hover"));
				}
			}
		}.setReuseItems(true));
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		headers.removeAll();
		for(IColumn<T> column : columns){
			headers.add(new Label(headers.newChildId(), new StringResourceModel(column.getPropertyName()+".label", this, null, column.getPropertyName())));
		}
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(CSS));
	}
	
	public void addColumn(IColumn<T> column){
		columns.add(column);
	}

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	protected abstract List<T> load(int page, int maxPerPage);
	
	protected void onSelected(AjaxRequestTarget target, IModel<T> model){ }
	
	public void refresh(){
		listView.removeAll();
	}
}
