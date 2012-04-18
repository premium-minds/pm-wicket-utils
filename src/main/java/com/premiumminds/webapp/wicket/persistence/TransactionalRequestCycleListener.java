package com.premiumminds.webapp.wicket.persistence;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import com.google.inject.persist.UnitOfWork;

public class TransactionalRequestCycleListener extends AbstractRequestCycleListener {
	private static final Logger log = Logger.getLogger(TransactionalRequestCycleListener.class);
	
	private UnitOfWork unitOfWork;
	private Provider<EntityManager> emp;
	
	public TransactionalRequestCycleListener(UnitOfWork unitOfWork, Provider<EntityManager> emp){
		this.unitOfWork = unitOfWork;
		this.emp = emp;
	}
	
	@Override
	public void onBeginRequest(RequestCycle cycle) {
		unitOfWork.begin();
		emp.get().getTransaction().begin();
		log.trace("Transaction started for request");
	}
	
	@Override
	public void onEndRequest(RequestCycle cycle) {
		EntityTransaction transaction = emp.get().getTransaction();
		if(transaction.getRollbackOnly()){
			transaction.rollback();
		} else {
			transaction.commit();
		}
		unitOfWork.end();
		log.trace("Transaction committed/rolledback for request");
	}	
	
	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		emp.get().getTransaction().setRollbackOnly();
		log.debug("Transaction set to rollback because of unhandled exception");
		return null;
	}
}
