/**
 * Copyright (C) 2016 Premium Minds.
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
