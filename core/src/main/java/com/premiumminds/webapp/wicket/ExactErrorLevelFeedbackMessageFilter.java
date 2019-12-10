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
