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
package com.premiumminds.webapp.wicket.bootstrap.datepicker;

import static org.junit.Assert.*;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.util.tester.TagTester;
import org.hamcrest.text.MatchesPattern;
import org.junit.Test;

import com.premiumminds.webapp.wicket.bootstrap.SpecialDate;
import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;

public class BootstrapDatePickerBehaviourTest extends AbstractComponentTest {
	@Test
	public void testTagInjection() {
		getTester().getSession().setLocale(Locale.ENGLISH);

		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour());
			}};
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester elem = TagTester.createTagByAttribute(doc, "wicket:id", "field");
		assertEquals("en", elem.getAttribute("data-date-language"));
	}

	@Test
	public void testCSSInjection() {
		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour());
			}};
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> css = TagTester.createTagsByAttribute(head.getValue(), "type", "text/css", false);
		assertEquals(1, css.size());
		assertEquals("link", css.get(0).getName());
		assertThat(css.get(0).getAttribute("href"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/datepicker(.*)\\.css"));
	}

	@Test
	public void testJSInjection() {
		getTester().getSession().setLocale(Locale.ENGLISH);

		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour());
			}};

		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> scripts = TagTester.createTagsByAttribute(head.getValue(), "type", "text/javascript", false);
		assertEquals(5, scripts.size());
		for (int i = 0; i < 5; i++) {
			assertEquals("script", scripts.get(i).getName());
		}
		
		assertThat(scripts.get(0).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker(.*)\\.js"));
		assertThat(scripts.get(1).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker-extension(.*)\\.js"));
		assertThat(scripts.get(2).getAttribute("src"), matchesPattern("(.*)" + JQueryResourceReference.class.getName() + "(.*)/jquery(.*)\\.js"));
		assertThat(scripts.get(3).getAttribute("src"), matchesPattern("(.*)" + AbstractDefaultAjaxBehavior.class.getName() + "(.*)/wicket-ajax-jquery(.*)\\.js"));
		assertThat(scripts.get(4).getValue(), matchesPattern("(?s)(.*)Wicket.Event.add\\(window, \\\"domready\\\", function\\(event\\) \\{(.*)\\$\\(\"#" +
				field.getMarkupId() + "\"\\).datepicker\\(\\);(.*);\\}\\);(.*)"));
	}

	@Test
	public void testJSInjectionNonEnglishLocale() {
		getTester().getSession().setLocale(Locale.FRENCH);

		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour());
			}};
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> scripts = TagTester.createTagsByAttribute(head.getValue(), "type", "text/javascript", false);
		assertEquals(6, scripts.size());
		for (int i = 0; i < 6; i++) {
			assertEquals("script", scripts.get(i).getName());
		}
		
		assertThat(scripts.get(0).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker(.*)\\.js"));
		assertThat(scripts.get(1).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker-extension(.*)\\.js"));
		assertThat(scripts.get(2).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/locales/bootstrap-datepicker.fr(.*)\\.js"));
		assertThat(scripts.get(3).getAttribute("src"), matchesPattern("(.*)" + JQueryResourceReference.class.getName() + "(.*)/jquery(.*)\\.js"));
		assertThat(scripts.get(4).getAttribute("src"), matchesPattern("(.*)" + AbstractDefaultAjaxBehavior.class.getName() + "(.*)/wicket-ajax-jquery(.*)\\.js"));
		assertThat(scripts.get(5).getValue(), matchesPattern("(?s)(.*)Wicket.Event.add\\(window, \\\"domready\\\", function\\(event\\) \\{(.*)\\$\\(\"#" +
				field.getMarkupId() + "\"\\).datepicker\\(\\);(.*);\\}\\);(.*)"));
	}

	@Test
	public void testJSInjectionOneSpecialDate() {
		getTester().getSession().setLocale(Locale.ENGLISH);

		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("deprecation")
					@Override
					public java.util.Collection<SpecialDate> getSpecialDates() {
						return Arrays.asList(new SpecialDate(new Date(110, 11, 25), "holiday", "Christmas"));
					};
				});
			}};
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> scripts = TagTester.createTagsByAttribute(head.getValue(), "type", "text/javascript", false);
		assertEquals(5, scripts.size());
		for (int i = 0; i < 5; i++) {
			assertEquals("script", scripts.get(i).getName());
		}

		assertThat(scripts.get(0).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker(.*)\\.js"));
		assertThat(scripts.get(1).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker-extension(.*)\\.js"));
		assertThat(scripts.get(2).getAttribute("src"), matchesPattern("(.*)" + JQueryResourceReference.class.getName() + "(.*)/jquery(.*)\\.js"));
		assertThat(scripts.get(3).getAttribute("src"), matchesPattern("(.*)" + AbstractDefaultAjaxBehavior.class.getName() + "(.*)/wicket-ajax-jquery(.*)\\.js"));
		assertThat(scripts.get(4).getValue(), matchesPattern("(?s)(.*)Wicket.Event.add\\(window, \\\"domready\\\", function\\(event\\) \\{(.*)\\$\\(\"#" +
				field.getMarkupId() + "\"\\).datepicker\\(null, \\[" +
				"\\{dt:new Date\\('2010-12-25'\\), css:'holiday', tooltip:'Christmas'\\}" +
				"\\]\\);(.*);\\}\\);(.*)"));
	}

	@Test
	public void testBehaviorManySpecialDates() {
		getTester().getSession().setLocale(Locale.ENGLISH);

		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("deprecation")
					@Override
					public java.util.Collection<SpecialDate> getSpecialDates() {
						return Arrays.asList(
								new SpecialDate(new Date(110, 11, 25), "holiday", "Christmas"),
								new SpecialDate(new Date(110, 0, 1), "holiday", "New Year"));
					};
				});
			}};
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> scripts = TagTester.createTagsByAttribute(head.getValue(), "type", "text/javascript", false);
		assertEquals(5, scripts.size());
		for (int i = 0; i < 5; i++) {
			assertEquals("script", scripts.get(i).getName());
		}
		assertThat(scripts.get(0).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker(.*)\\.js"));
		assertThat(scripts.get(1).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker-extension(.*)\\.js"));
		assertThat(scripts.get(2).getAttribute("src"), matchesPattern("(.*)" + JQueryResourceReference.class.getName() + "(.*)/jquery(.*)\\.js"));
		assertThat(scripts.get(3).getAttribute("src"), matchesPattern("(.*)" + AbstractDefaultAjaxBehavior.class.getName() + "(.*)/wicket-ajax-jquery(.*)\\.js"));

		assertThat(scripts.get(4).getValue(), matchesPattern("(?s)(.*)Wicket.Event.add\\(window, \\\"domready\\\", function\\(event\\) \\{(.*)\\$\\(\"#" +
				field.getMarkupId() + "\"\\).datepicker\\(null, \\[" +
				"\\{dt:new Date\\('2010-12-25'\\), css:'holiday', tooltip:'Christmas'\\}," +
				"\\{dt:new Date\\('2010-01-01'\\), css:'holiday', tooltip:'New Year'\\}" +
				"\\]\\);(.*);\\}\\);(.*)"));
	}

	@Test
	public void testBehaviorEmptySpecialDates() {
		getTester().getSession().setLocale(Locale.ENGLISH);

		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour() {
					private static final long serialVersionUID = 1L;

					@Override
					public java.util.Collection<SpecialDate> getSpecialDates() {
						return new ArrayList<SpecialDate>();
					};
				});
			}};
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> scripts = TagTester.createTagsByAttribute(head.getValue(), "type", "text/javascript", false);
		assertEquals(5, scripts.size());
		for (int i = 0; i < 5; i++) {
			assertEquals("script", scripts.get(i).getName());
		}

		assertThat(scripts.get(0).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker(.*)\\.js"));
		assertThat(scripts.get(1).getAttribute("src"), matchesPattern("(.*)" + BootstrapDatePickerBehaviour.class.getName() + "/bootstrap-datepicker-extension(.*)\\.js"));
		assertThat(scripts.get(2).getAttribute("src"), matchesPattern("(.*)" + JQueryResourceReference.class.getName() + "(.*)/jquery(.*)\\.js"));
		assertThat(scripts.get(3).getAttribute("src"), matchesPattern("(.*)" + AbstractDefaultAjaxBehavior.class.getName() + "(.*)/wicket-ajax-jquery(.*)\\.js"));
		assertThat(scripts.get(4).getValue(), matchesPattern("(?s)(.*)Wicket.Event.add\\(window, \\\"domready\\\", function\\(event\\) \\{(.*)\\$\\(\"#" +
				field.getMarkupId() + "\"\\).datepicker\\(\\);(.*);\\}\\);(.*)"));

	}

	@Test
	public void testNonInjectionInDisabledComponent() {
		DateTextField field = new DateTextField("field") {
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDatePickerBehaviour());
			}};
		field.setEnabled(false);
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		assertNull(TagTester.createTagByAttribute(doc, "head"));

		TagTester elem = TagTester.createTagByAttribute(doc, "wicket:id", "field");
		assertNull(elem.getAttribute("data-date-language"));
	}
}
