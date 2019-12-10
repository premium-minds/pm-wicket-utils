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
package com.premiumminds.webapp.wicket.persistence;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import com.premiumminds.persistence.PersistenceTransaction;

public class TransactionalRequestCycleListener implements IRequestCycleListener {
	private PersistenceTransaction persistenceTransaction;
	
	public TransactionalRequestCycleListener(PersistenceTransaction persistenceTransaction){
		this.persistenceTransaction = persistenceTransaction;
	}
	
	@Override
	public void onBeginRequest(RequestCycle cycle) {
		persistenceTransaction.start();
	}
	
	@Override
	public void onEndRequest(RequestCycle cycle) {
		persistenceTransaction.end();
	}	
	
	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		persistenceTransaction.setRollbackOnly();
		return null;
	}
}
