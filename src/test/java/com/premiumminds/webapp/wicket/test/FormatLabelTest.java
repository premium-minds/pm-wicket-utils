package com.premiumminds.webapp.wicket.test;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.premiumminds.webapp.wicket.FormatLabel;

public class FormatLabelTest {

	private WicketTester tester;
	private double tau = 6.28318530717958647692; 
	
	public class Page extends WebPage {

		private static final long serialVersionUID = 2625516294774975788L;
		
		@Override
		protected void onInitialize() {
			super.onInitialize();

			FormatLabel<Double> format = new FormatLabel<Double>("id", Model.of(tau));
			add(format);
		}
	}
	
	@Before
	public void setUp(){
		tester = new WicketTester(new WebApplication() {
			
			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}
			
			@Override
			public Session newSession(Request request, Response response) {
				Session session = super.newSession(request, response);
				session.setLocale(Locale.FRENCH);
				return session;
			}
		});
	}
	
	@Test
	public void test(){
		tester.startPage(new Page());
		tester.assertRenderedPage(Page.class);
		tester.assertModelValue("id", tau);
		tester.assertLabel("id", "6,283 ºC");
	}
}
