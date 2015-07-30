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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Drawer base class, to be used with {@link DrawerManager}.
 *
 */
public abstract class AbstractDrawer extends Panel {
	private static final long serialVersionUID = -9059038999170018388L;

	public static final String DRAWER_ID = "drawer";

	private DrawerManager manager;
	private boolean allowClose = true; 

	public AbstractDrawer() {
		super(DRAWER_ID);
		setOutputMarkupId(true);
	}

	/**
	 * Override this method to be notified when the drawer is about to be closed.
	 * If you setAllowClose(false), the drawer will NOT close.
	 * 
	 * @param target
	 * 			The current AJAX target. You can use this to push further behavior up to the client.
	 */
	protected void beforeClose(AjaxRequestTarget target) {
	}

	/**
	 * Used by the drawer manager when the drawer is pushed onto the stack.
	 * Do not call this method outside of these circumstances.
	 * 
	 * @param manager
	 * 			A reference to the drawer manager
	 */
	/*package-private*/ void setManager(DrawerManager manager) {
		this.manager = manager;
	}

	/**
	 * @return
	 * 			A reference to the last drawer manager that used this drawer object.
	 */
	public DrawerManager getManager() {
		return manager;
	}

	/**
	 * Sets a flag which controls whether the drawer can be closed from the browser.
	 * In particular, this function can be called from {@link #beforeClose(AjaxRequestTarget target)}
	 * to cancel a close event.
	 * 
	 * Note that trying to close the drawer from code results in a call to setAllowClose(true).
	 * 
	 * @param allowClose
	 * 			The value of the flag
	 */
	public void setAllowClose(boolean allowClose) {
		this.allowClose = allowClose;
	}

	/**
	 * @return
	 * 			The current value of the flag that controls whether the drawer can be
	 * 			closed from the browser. See {@link #setAllowClose(boolean allowClose)}.
	 */
	public boolean isAllowClose() {
		return allowClose;
	}
}
