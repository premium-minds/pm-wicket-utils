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
package com.premiumminds.webapp.wicket.bootstrap;

import static org.junit.Assert.*;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapDatepicker;

public class BootstrapDatepickerTest {
	
	private BootstrapDatepicker component;
	private DateTextField field;

	@Test
	public void testRender() {
		WicketTester tester = createTester(true);
		
		TagTester tag = tester.getTagByWicketId("datepicker");
		
		assertTrue(tag.hasAttribute("data-date-format"));
		assertEquals(field.getTextFormat().toLowerCase(), tag.getAttribute("data-date-format"));
	}
	
	@Test
	public void testLanguage(){
		WicketTester tester = createTester(false);
		
		TagTester tag = tester.getTagByWicketId("datepicker");
		
		assertTrue(tag.hasAttribute("data-date-language"));
		assertEquals("fr", tag.getAttribute("data-date-language"));
		
		// would be cool to test if js file was also included
	}
	
	
	private WicketTester createTester(boolean enabled){
		WicketTester tester = new WicketTester(new WebApplication() {
			
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
		}){
			@Override
			protected String createPageMarkup(String componentId) {
				return "<div class=\"date\" wicket:id=\"datepicker\">"+
						"	<input size=\"16\" type=\"text\" class=\"input-small\" wicket:id=\"input\">"+
						"	<span class=\"add-on\"><i class=\"icon-calendar\"></i></span>"+
						"</div>";
			}
		};
		
		component = new BootstrapDatepicker("datepicker");
		
		component.add(field = new DateTextField("input"));
		
		tester.startComponentInPage(component);
		return tester;
	}
}
