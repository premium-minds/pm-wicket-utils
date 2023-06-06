package com.premiumminds.webapp.wicket.persistence;

import com.premiumminds.persistence.PersistenceTransaction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class TransactionalRequestCycleListenerTest {

    @Test
    public void testSuccess(){

        PersistenceTransaction transaction = EasyMock.mock(PersistenceTransaction.class);

        // 2 times because of a redirect
        transaction.start();
        EasyMock.expectLastCall().times(2);
        transaction.end();
        EasyMock.expectLastCall().times(2);

        EasyMock.replay(transaction);

        TransactionalRequestCycleListener listener = new TransactionalRequestCycleListener(transaction);

        WicketTester tester = new WicketTester();
        tester.getApplication().getRequestCycleListeners().add(listener);

        tester.startComponentInPage(new Label("text", new Model<>()));

        EasyMock.verify(transaction);
    }

    @Test
    public void testOnExceptionRollback(){

        PersistenceTransaction transaction = EasyMock.mock(PersistenceTransaction.class);

        transaction.start();
        transaction.setRollbackOnly();
        transaction.end();

        EasyMock.replay(transaction);

        TransactionalRequestCycleListener listener = new TransactionalRequestCycleListener(transaction);

        WicketTester tester = new WicketTester();
        tester.getApplication().getRequestCycleListeners().add(listener);

        try{
            tester.startComponentInPage(new TextField<>("text", new Model<>()));
        }catch (Exception e){

        }

        EasyMock.verify(transaction);
    }
}
