package com.premiumminds.webapp.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class DynamicListItem extends Panel {

	public DynamicListItem(String id) {
		super(id);
		add(new EmptyPanel("next").setOutputMarkupPlaceholderTag(true));
	}
	
	public void removeItem(AjaxRequestTarget target){
		Panel panel = new EmptyPanel("container");
		addOrReplace(panel);
		panel.setOutputMarkupId(true);
		panel.setVisible(false);
		target.add(panel);
	}
	
	public void setContainer(Panel panel){
		add(panel);
		panel.setOutputMarkupId(true);
	}
	
	public void setNext(AjaxRequestTarget target, Panel panel){
		panel.setOutputMarkupPlaceholderTag(true);
		addOrReplace(panel);
		target.add(panel);
	}

}
