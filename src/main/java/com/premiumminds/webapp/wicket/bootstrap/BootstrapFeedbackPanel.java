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
package com.premiumminds.webapp.wicket.bootstrap;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.premiumminds.webapp.wicket.UniqueFeedbackMessageFilter;

public class BootstrapFeedbackPanel extends FeedbackPanel {
	private static final long serialVersionUID = -4977308779783507462L;
	
	private IFeedbackMessageFilter filter;

	public BootstrapFeedbackPanel(String id) {
		super(id);
		uniqueMessages();
	}

	public BootstrapFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id);
		this.filter = filter;
		uniqueMessages();
	}

	public BootstrapFeedbackPanel(String id, final int level) {
		super(id);
		
		this.filter = (new IFeedbackMessageFilter() {
			private static final long serialVersionUID = -664394776030705182L;

			@Override
			public boolean accept(final FeedbackMessage message) {
				if(BootstrapFeedbackPanel.this.getParent()!=null){
					BootstrapFeedbackPopover innerAccepted = BootstrapFeedbackPanel.this.getParent()
							.visitChildren(BootstrapFeedbackPopover.class, new IVisitor<BootstrapFeedbackPopover, BootstrapFeedbackPopover>() {
	
						@Override
						public void component(BootstrapFeedbackPopover object, IVisit<BootstrapFeedbackPopover> visit) {
							if(object.getFilter().accept(message)){
								visit.stop(object);
							}
						}
					});
					
					return innerAccepted==null && message.getLevel()==level;
				}
				return message.getLevel()==level;
			}
		});
		uniqueMessages();
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		setOutputMarkupPlaceholderTag(true);
		
		add(new WebMarkupContainer("close"){
			private static final long serialVersionUID = 1566780832755857170L;

			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);
				
				StringBuilder sb = new StringBuilder();
				sb.append("$('#"+this.getMarkupId()+"').click(function(){");
				sb.append("  $('#"+BootstrapFeedbackPanel.this.getMarkupId()+"').hide();");
				sb.append("})");
				
				response.render(OnDomReadyHeaderItem.forScript(sb.toString()));
			}
		});
	}
	
	/**
	 * Enable filter to only display unique feedback messages (enabled by default)
	 * @return this panel for fluent api
	 */
	public BootstrapFeedbackPanel uniqueMessages(){
		setFilter(new UniqueFeedbackMessageFilter(filter));
		return this;
	}
	
	/**
	 * Disable filter to allow displaying repeated feedback messages
	 * @return this panel for fluent api
	 */
	public BootstrapFeedbackPanel allowRepeatedMessages(){
		setFilter(new UniqueFeedbackMessageFilter(filter));
		return this;
	}
	
	@Override
	protected void onConfigure() {
		setVisible(anyMessage());
	}
}
