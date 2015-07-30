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

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.util.visit.IVisit;

import com.premiumminds.webapp.wicket.UniqueFeedbackMessageFilter;

public class BootstrapFeedbackPanel extends FeedbackPanel {
	private static final long serialVersionUID = 918157933592698927L;

	private IFeedbackMessageFilter filter;

	/**
	 * Constructor. builds a feedback panel which filters messages based on the message
	 * level
	 *
	 * @param id
	 * 				the component id.
	 * @param level
	 * 				the message level
	 */
	public BootstrapFeedbackPanel(String id, int level) {
		this(id, new FeedbackMessageLevelFilter(level));
	}

	/**
	 * Constructor. Builds a feedback panel which filters messages based on the component
	 * 
	 * @param id
	 * 				the component id
	 * @param componentToFilter
	 * 				catch only feedback from this component
	 *
	 * @see org.apache.wicket.markup.html.panel.ComponentFeedbackPanel
	 * 
	 */
	public BootstrapFeedbackPanel(String id, Component componentToFilter) {
		this(id, new ComponentFeedbackMessageFilter(componentToFilter));
	}

	/**
	 * Constructor. Builds a feedback panel which filters messages based on the component
	 * 
	 * @param id
	 * 				the component id
	 * @param level
	 * 				the message level
	 * @param componentToFilter
	 * 				catch only feedback from this component
	 * 
	 * @see org.apache.wicket.markup.html.panel.ComponentFeedbackPanel
	 */
	public BootstrapFeedbackPanel(String id, final int level, final Component componentToFilter) {
		this(id, new AndComposedFeedbackMessageFilter(
					new ComponentFeedbackMessageFilter(componentToFilter),
					new FeedbackMessageLevelFilter(level)));
		return;
	}

	/**
	 * Constructor. Builds a feedback panel which filters messages based on the component
	 * 
	 * @param id
	 * 				the component id
	 * @see org.apache.wicket.markup.html.panel.ComponentFeedbackPanel
	 */
	public BootstrapFeedbackPanel(String id) {
		super(id, null);
	}

	/**
	 * Constructor. Builds a feedback panel which filters messages based on the component
	 * 
	 * @param id
	 * 				the component id
	 * @param filter
	 * 				filter to be applied to each message
	 * 
	 * @see org.apache.wicket.markup.html.panel.ComponentFeedbackPanel
	 */
	public BootstrapFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id);
		this.filter = filter;
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
	
	@Override
	protected void onConfigure() {
		setVisible(anyMessage());
	}

	/**
	 * Enable filter to only display unique feedback messages (enabled by default)
	 * @return this panel for fluent api
	 */
	public BootstrapFeedbackPanel uniqueMessages(){
		if(filter!=null){
			setFilter(new AndComposedFeedbackMessageFilter(new UniqueFeedbackMessageFilter(), new ExcludePopoverMessageFilter(), filter));
		} else {
			setFilter(new AndComposedFeedbackMessageFilter(new UniqueFeedbackMessageFilter(), new ExcludePopoverMessageFilter()));
		}
		return this;
	}
	
	/**
	 * Disable filter to allow displaying repeated feedback messages
	 * @return this panel for fluent api
	 */
	public BootstrapFeedbackPanel allowRepeatedMessages(){
		setFilter(filter);
		return this;
	}

	private static class FeedbackMessageLevelFilter implements IFeedbackMessageFilter {
		private static final long serialVersionUID = 4966186682921209451L;
		final int level;

		public FeedbackMessageLevelFilter(int level) {
			this.level = level;
		}

		public boolean accept(FeedbackMessage m) {
			return m.getLevel()==level;
		}
	}

	private static class AndComposedFeedbackMessageFilter implements IFeedbackMessageFilter {
		private static final long serialVersionUID = 1L;
		final protected IFeedbackMessageFilter[] filters;

		protected AndComposedFeedbackMessageFilter(IFeedbackMessageFilter... filters) {
			this.filters = filters;
		}
		
		public boolean accept(FeedbackMessage message) {
			for ( IFeedbackMessageFilter f : filters ) {
				if ( !f.accept(message) ) {
					return false;
				}
			}
			return true;
		}
	}
	
	private class ExcludePopoverMessageFilter implements IFeedbackMessageFilter {
		private static final long serialVersionUID = 1488859172984813599L;

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
				
				return innerAccepted==null;
			}
			return true;
		}
		
	}
}
