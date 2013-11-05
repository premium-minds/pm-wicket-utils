package com.premiumminds.webapp.wicket.persistence;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import com.premiumminds.persistence.PersistenceTransaction;

public class TransactionalRequestCycleListener extends AbstractRequestCycleListener {
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
