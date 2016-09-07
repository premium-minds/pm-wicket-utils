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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.util.tester.TagTester;
import org.junit.Test;

import com.premiumminds.webapp.wicket.bootstrap.datetimepicker.BootstrapDateTimePickerBehaviour;
import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;

public class BootstrapDateTimePickerBehaviourTest extends AbstractComponentTest 
{
	@Test
	public void testCSSInjection() 
	{
		DateTextField field = new DateTextField("field") {
			
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDateTimePickerBehaviour());
			}
		};
		
		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> css = TagTester.createTagsByAttribute(head.getValue(), "type", "text/css", false);
		assertEquals(1, css.size());
		assertEquals("link", css.get(0).getName());
		assertTrue(css.get(0).getAttribute("href").matches("(.*)" + BootstrapDateTimePickerBehaviour.class.getName() + "/bootstrap-datetimepicker(.*)\\.css"));
	}

	@Test
	public void testJSInjection() 
	{
		getTester().getSession().setLocale(Locale.ENGLISH);

		DateTextField field = new DateTextField("field") {
			
			private static final long serialVersionUID = 1L;

			{
				add(new BootstrapDateTimePickerBehaviour());
			}
		};

		startFormComponentTest(field, "text");

		String doc = getTester().getLastResponse().getDocument();

		TagTester head = TagTester.createTagByAttribute(doc, "head");
		assertNotNull(head);

		List<TagTester> scripts = TagTester.createTagsByAttribute(head.getValue(), "type", "text/javascript", false);
		
		assertTrue(scripts.get(0).getAttribute("src").matches("(.*)" + BootstrapDateTimePickerBehaviour.class.getName() + "/moment-with-locales(.*)\\.js"));
		assertTrue(scripts.get(1).getAttribute("src").matches("(.*)" + BootstrapDateTimePickerBehaviour.class.getName() + "/bootstrap-datetimepicker(.*)\\.js"));
	}
}
