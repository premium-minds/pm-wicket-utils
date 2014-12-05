package com.premiumminds.webapp.wicket.drawer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractDrawer extends Panel {
	private static final long serialVersionUID = -9059038999170018388L;

	public static final String DRAWER_ID = "drawer";
	public static final String HEADER_ID = "header";
	public static final String CONTENT_ID = "content";
	public static final String FOOTER_ID = "footer";

	private DrawerManager manager;
	private boolean allowClose = true; 

	public AbstractDrawer() {
		super(DRAWER_ID);
		setOutputMarkupId(true);
	}

	protected Panel getHeader(String id) {
		return new EmptyPanel(id);
	}

	protected Panel getContent(String id) {
		return new EmptyPanel(id);
	}

	protected Panel getFooter(String id) {
		return new EmptyPanel(id);
	}

	protected void onClose(AjaxRequestTarget target) {
	}

	public void setManager(DrawerManager manager){
		this.manager = manager;
	}
	
	public DrawerManager getManager(){
		return manager;
	}

	public void setAllowClose(boolean allowClose) {
		this.allowClose = allowClose;
	}

	public boolean isAllowClose() {
		return allowClose;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(getHeader(HEADER_ID));
		add(getContent(CONTENT_ID));
		add(getFooter(FOOTER_ID));
	}
}
