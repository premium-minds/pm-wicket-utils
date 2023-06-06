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

import java.util.Locale;

import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;
import com.premiumminds.webapp.wicket.testing.ExtendedWicketTester;
import org.apache.wicket.markup.ContainerInfo;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.TagTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class BooleanDropDownTest extends AbstractComponentTest {
	private Boolean inputBoolean;

	@Test
	public void testInitialization() {
		inputBoolean = true;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean));
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		getTester().assertComponent(dropDown.getPageRelativePath(), BooleanDropDown.class);
		getTester().assertModelValue(dropDown.getPageRelativePath(), true);
	}

	@Test
	public void testStartingNull() {
		inputBoolean = null;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean));
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		TagTester selectTag = getTester().getTagById(dropDown.getMarkupId());
		TagTester firstOption = selectTag.getChild("option");
		TagTester trueOption = selectTag.getChild("value", "true");
		TagTester falseOption = selectTag.getChild("value", "false");
		
		assertEquals(firstOption.getValue(), "Choose");
		assertEquals(firstOption.getAttribute("selected"), "selected");
		assertEquals(trueOption.getValue(), "Yes");
		assertEquals(falseOption.getValue(), "No");
	}

	@Test
	public void testNullNotAvailable() {
		inputBoolean = true;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean));
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		TagTester selectTag = getTester().getTagById(dropDown.getMarkupId());
		TagTester firstOption = selectTag.getChild("option");
		TagTester trueOption = selectTag.getChild("value", "true");
		TagTester falseOption = selectTag.getChild("value", "false");
		
		assertNotEquals(firstOption.getValue(), "Choose");
		assertEquals(trueOption.getAttribute("selected"), "selected");
		assertNull(falseOption.getAttribute("selected"));
	}

	@Test
	public void testRequiredStartingNull() {
		inputBoolean = null;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean), true);
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		TagTester selectTag = getTester().getTagById(dropDown.getMarkupId());
		TagTester firstOption = selectTag.getChild("option");
		TagTester trueOption = selectTag.getChild("value", "true");
		TagTester falseOption = selectTag.getChild("value", "false");
		
		assertEquals(firstOption.getValue(), "Choose");
		assertEquals(firstOption.getAttribute("selected"), "selected");
		assertNull(trueOption.getAttribute("selected"));
		assertNull(falseOption.getAttribute("selected"));
	}

	@Test
	public void testRequiredNullNotAvailable() {
		inputBoolean = false;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean), true);
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		TagTester selectTag = getTester().getTagById(dropDown.getMarkupId());
		TagTester firstOption = selectTag.getChild("option");
		TagTester trueOption = selectTag.getChild("value", "true");
		TagTester falseOption = selectTag.getChild("value", "false");
		
		assertNotEquals(firstOption.getValue(), "Choose");
		assertNull(trueOption.getAttribute("selected"));
		assertEquals(falseOption.getAttribute("selected"), "selected");
	}

	@Test
	public void testRevertableStartingNull() {
		inputBoolean = null;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean), true);
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		TagTester selectTag = getTester().getTagById(dropDown.getMarkupId());
		TagTester firstOption = selectTag.getChild("option");
		TagTester trueOption = selectTag.getChild("value", "true");
		TagTester falseOption = selectTag.getChild("value", "false");
		
		assertEquals(firstOption.getValue(), "Choose");
		assertEquals(firstOption.getAttribute("selected"), "selected");
		assertNull(trueOption.getAttribute("selected"));
		assertNull(falseOption.getAttribute("selected"));
	}

	@Test
	public void testRevertableNullAvailableToRevert() {
		inputBoolean = true;
		BooleanDropDown dropDown = new BooleanDropDown("id", Model.of(inputBoolean), false, true);
		dropDown.setOutputMarkupId(true);
		getTester().getSession().setLocale(Locale.ENGLISH);
		startFormComponentTestWithMarkup(dropDown, "select", createFormPageMarkup("id"));

		TagTester selectTag = getTester().getTagById(dropDown.getMarkupId());
		TagTester firstOption = selectTag.getChild("option");
		TagTester trueOption = selectTag.getChild("value", "true");
		TagTester falseOption = selectTag.getChild("value", "false");
		
		assertEquals(firstOption.getValue(), "Choose");
		assertEquals(trueOption.getAttribute("selected"), "selected");
		assertNull(falseOption.getAttribute("selected"));
	}

	private IMarkupFragment createFormPageMarkup(final String componentId) {
		String markup = /*"<html><head></head>" +*/ "<body><form wicket:id='form'><select wicket:id='" + componentId
				+ "' /></form></body>" /*+ "</html>"*/;

		try {
			// set a ContainerInfo to be able to use HtmlHeaderContainer so header contribution
			// still work. WICKET-3700
			ContainerInfo containerInfo = new ContainerInfo(new ExtendedWicketTester.StartComponentInForm());
			MarkupResourceStream markupResourceStream = new MarkupResourceStream(new StringResourceStream(markup),
					containerInfo, ExtendedWicketTester.StartComponentInForm.class);

			MarkupParser markupParser = getTester().getApplication().getMarkupSettings().getMarkupFactory()
					.newMarkupParser(markupResourceStream);
			return markupParser.parse();
		} catch (Exception e) {
			fail("Error while parsing the markup for the autogenerated page: " + e.getMessage());
			return null;
		}
	}
}
