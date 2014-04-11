package com.premiumminds.webapp.wicket.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

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
	 * @see org.apache.wicket.markup.html.panel.ComponentFeedbackPanel
	 * 
	 */
	public BootstrapFeedbackPanel(String id, Component componentToFilter) {
		this(id, new ComponentFeedbackMessageFilter(componentToFilter));
	}

	public BootstrapFeedbackPanel(String id, final int level, final Component componentToFilter) {
		this(id, new AndComposedFeedbackMessageFilter(
					new ComponentFeedbackMessageFilter(componentToFilter),
					new FeedbackMessageLevelFilter(level)));
		return;
	}

	public BootstrapFeedbackPanel(String id) {
		super(id, null);
	}

	public BootstrapFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id);
		this.filter = filter;
		uniqueMessages();
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
			setFilter(new AndComposedFeedbackMessageFilter(new UniqueFeedbackMessageFilter(), filter));
		} else {
			setFilter(new UniqueFeedbackMessageFilter());
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
}
