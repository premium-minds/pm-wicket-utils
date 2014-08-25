package com.premiumminds.webapp.wicket;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

/**
 * Filter that matches EXACTLY according to the provided error level
 * (cf. Wicket's ErrorLevelFeedbackMessageFilter)
 * 
 * Example use:
 * 
 * IFeedbackMessageFilter errorFilter =
 *   new ExactErrorLevelFilter(FeedbackMessage.ERROR);
 */
public class ExactErrorLevelFeedbackMessageFilter implements IFeedbackMessageFilter {
	private static final long serialVersionUID = 1L;
	
	private int errorLevel;

	public ExactErrorLevelFeedbackMessageFilter(int errorLevel){
		this.errorLevel = errorLevel;
	}

	public boolean accept(FeedbackMessage message) {
		return message.getLevel() == errorLevel;
	}

}
