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
