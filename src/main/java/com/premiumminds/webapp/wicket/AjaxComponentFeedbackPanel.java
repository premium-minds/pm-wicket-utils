package com.premiumminds.webapp.wicket;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AjaxComponentFeedbackPanel extends Panel implements IFeedback {
	private static final long serialVersionUID = -7684620498339448883L;

	private IFeedbackMessageFilter filter;
	
	private Component messageHolder;
	
	public AjaxComponentFeedbackPanel(String id) {
		super(id);
		this.setOutputMarkupId(true);
	}
	
	public void setFeedbackFor(Component component){
		filter = new ComponentFeedbackMessageFilter(component);
	}
	
	protected void setMessageHolder(Component component){
		messageHolder = component;
	}
	
	abstract protected void onDisplayError(FeedbackMessage message);

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		FeedbackMessages feedbackMessages = Session.get().getFeedbackMessages();
		
		if(feedbackMessages.hasMessage(getFeedbackMessageFilter())){
			messageHolder.setVisible(true);
			List<FeedbackMessage> messages = feedbackMessages.messages(getFeedbackMessageFilter());
			FeedbackMessage message = messages.get(0);
			onDisplayError(message);
			message.markRendered();
		} else {
			messageHolder.setVisible(false);
		}
	}
	
	protected IFeedbackMessageFilter getFeedbackMessageFilter(){
		return filter;
	}
}
