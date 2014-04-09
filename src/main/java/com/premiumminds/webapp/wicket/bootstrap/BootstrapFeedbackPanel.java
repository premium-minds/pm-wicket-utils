package com.premiumminds.webapp.wicket.bootstrap;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
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
