package com.premiumminds.webapp.wicket;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.ILogData;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.IContextProvider;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractComponentTest extends EasyMockSupport implements IContextProvider<AjaxRequestTarget, Page> {
	private class RequestTargetTester implements AjaxRequestTarget {
		private AjaxRequestTarget inner;

		@SuppressWarnings("unused")
		public RequestTargetTester(Page page) {
			inner = new AjaxRequestHandler(page);
		}

		@Override
		public Integer getPageId() {
			return inner.getPageId();
		}

		@Override
		public boolean isPageInstanceCreated() {
			return inner.isPageInstanceCreated();
		}

		@Override
		public Integer getRenderCount() {
			return inner.getRenderCount();
		}

		@Override
		public Class<? extends IRequestablePage> getPageClass() {
			return inner.getPageClass();
		}

		@Override
		public PageParameters getPageParameters() {
			return inner.getPageParameters();
		}

		@Override
		public void respond(IRequestCycle requestCycle) {
			inner.respond(requestCycle);
		}

		@Override
		public void detach(IRequestCycle requestCycle) {
			inner.detach(requestCycle);
		}

		@Override
		public ILogData getLogData() {
			return inner.getLogData();
		}

		@Override
		public void add(Component component, String markupId) {
			inner.add(component, markupId);
		}

		@Override
		public void add(Component... components) {
			inner.add(components);
		}

		@Override
		public void addChildren(MarkupContainer parent, Class<?> childCriteria) {
			inner.addChildren(parent, childCriteria);
		}

		@Override
		public void addListener(IListener listener) {
			inner.addListener(listener);
		}

		@Override
		public void appendJavaScript(CharSequence javascript) {
			inner.appendJavaScript(javascript);
		}

		@Override
		public void prependJavaScript(CharSequence javascript) {
			inner.prependJavaScript(javascript);
		}

		@Override
		public void registerRespondListener(ITargetRespondListener listener) {
			inner.registerRespondListener(listener);
		}

		@Override
		public Collection<? extends Component> getComponents() {
			return inner.getComponents();
		}

		@Override
		public void focusComponent(Component component) {
			inner.focusComponent(component);
		}

		@Override
		public IHeaderResponse getHeaderResponse() {
			return inner.getHeaderResponse();
		}

		@Override
		public String getLastFocusedElementId() {
			return inner.getLastFocusedElementId();
		}

		@Override
		public Page getPage() {
			return inner.getPage();
		}
		
	}

	private static final Method[] methods = initMethods(); 

	private WebApplication wicketApp;
	private WicketTester tester;
	private IContextProvider<AjaxRequestTarget, Page> orig;
	private AjaxRequestTarget target;
	private boolean running;

	public AbstractComponentTest() {
		wicketApp = new WebApplication() {
			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}};
	}

	@Before
	public void setUp() {
		tester = new WicketTester(wicketApp);
		running = false;
	}

	@After
	public void tearDown() {
		if (running) {
			running = false;
			wicketApp.setAjaxRequestTargetProvider(orig);
		}
	}

	protected void startTest(Component subject) {
		if (!running) {
			tester.startComponentInPage(subject);
			target = createMockBuilder(RequestTargetTester.class)
					.addMockedMethods(methods)
					.withConstructor(this, tester.getLastRenderedPage())
					.createStrictMock();
			orig = wicketApp.getAjaxRequestTargetProvider();
			wicketApp.setAjaxRequestTargetProvider(this);
			running = true;
		}
	}

	protected WicketTester getTester() {
		return tester;
	}


	protected AjaxRequestTarget getTarget() throws TestNotStartedException {
		if (!running)
			throw new TestNotStartedException();

		return target;
	}

	@Override
	public AjaxRequestTarget get(Page context) {
		return target;
	}

	//This method may need to be expanded if we find further unexpected calls into the request target
	//from inside the Wicket framework - JMMM
	private static Method[] initMethods() {
		HashSet<Method> aux = new HashSet<Method>(Arrays.asList(RequestTargetTester.class.getDeclaredMethods()));
		aux.remove(getMethodHelper("addListener", AjaxRequestTarget.IListener.class));
		aux.remove(getMethodHelper("respond", IRequestCycle.class));
		return aux.toArray(new Method[aux.size()]);
	}

	private static Method getMethodHelper(String name, Class<?>... parameterTypes) {
		try {
			return RequestTargetTester.class.getDeclaredMethod(name, parameterTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
}
