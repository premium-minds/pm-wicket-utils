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

/**
 * Filter that matches messages that are matched by at least one of the added 
 * filters (thus implementing a logical OR operation). 
 * Allows for flexible composition of existing filters.
 * 
 * Example use (filters provided in constructor):
 * IFeedbackMessageFilter filter = new CompositeAndMessageFilter(
 *     new ComponentFeedbackMessageFilter(someComponent),
 *     new ComponentFeedbackMessageFilter(someOtherComponent)
 * );
 * 
 * Example use (with chaining):
 * IFeedbackMessageFilter filter = new CompositeAndMessageFilter()
 *     .add(new ComponentFeedbackMessageFilter(someComponent))
 *     .add(new ComponentFeedbackMessageFilter(someOtherComponent));
 */
public class OrMessageFilter implements IFeedbackMessageFilter {
	private static final long serialVersionUID = -162869656149627953L;
	
	private List<IFeedbackMessageFilter> filters;
	
	public OrMessageFilter() {
		filters = new ArrayList<IFeedbackMessageFilter>();
		filters.add(IFeedbackMessageFilter.NONE);
	}
	
	public OrMessageFilter(IFeedbackMessageFilter... filters) {
		this();
		add(filters);
	}
	
	/**
	 * @param message
	 *            The message to test for inclusion
	 * @return True if the message should be included, false to exclude it
	 */
	@Override
	public boolean accept(FeedbackMessage message) {
		for (IFeedbackMessageFilter filter : filters) {
			if (filter.accept(message)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add the provided filter(s). Allows chaining.
	 * @param filters The filter(s) to be added
	 * @return This CompositeAndMessageFilter, to allow chaining
	 */
	public OrMessageFilter add(IFeedbackMessageFilter... filters) {
		for (IFeedbackMessageFilter filter : filters) {
			this.filters.add(filter);
		}
		return this;
	}

}
