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

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.protocol.http.WebApplication;

import com.premiumminds.webapp.wicket.repeaters.AjaxListSetView;

public class Initializer implements IInitializer {

	@Override
	public void init(Application application) {
		if(application instanceof WebApplication){
			WebApplication app = (WebApplication) application;
			
			// add DynamicListView handler
			app.getAjaxRequestTargetListeners().add(new AjaxListSetView.AjaxListener());
		}
	}

	@Override
	public void destroy(Application application) {
	}

	@Override
	public String toString() {
		return "pm-wicket-utils initializer";
	}
}
