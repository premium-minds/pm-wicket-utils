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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

public class UniqueFeedbackMessageFilter implements IFeedbackMessageFilter {
	private static final long serialVersionUID = 1230282488271953295L;
	
	private List<FeedbackMessage> messages = new ArrayList<FeedbackMessage>();
	
	public void clearMessages(){
		messages.clear();
	}

	@Override
	public boolean accept(FeedbackMessage message) {
		// too bad that FeedbackMessage doesnt have an equals implementation
		for(FeedbackMessage m : messages){
			if(m.getMessage().toString().equals(message.getMessage())) return false;
		}
		return true;
	}

}
