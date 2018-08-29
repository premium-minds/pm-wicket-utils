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
package com.premiumminds.webapp.wicket.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupFragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;

public class BootstrapDateTimePickerTest extends AbstractComponentTest {
	private class TestBootstrapDateTimePicker extends BootstrapDateTimePicker {
		private static final long serialVersionUID = 1L;

		private DateTextField field;

		public TestBootstrapDateTimePicker(String id) {
			super(id);

			field = new DateTextField("input", new Model<Date>());
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();
			add(field);
		}

		@Override
		public IMarkupFragment getMarkup() {
			IMarkupFragment all = getAssociatedMarkup();
			for (int i = 0; i < all.size(); i++ ) {
				if (!all.get(i).toString().startsWith("<!--"))
					return new MarkupFragment(all,  i);
			}

			return all;
		}
	}

	@Test
	public void testModel() {
		TestBootstrapDateTimePicker picker = new TestBootstrapDateTimePicker("picker");

		getTester().startComponentInForm(picker, "date");
		FormTester formTester = getTester().newFormTester("form");
		assertNull(picker.getModelObject());
		formTester.setValue(picker.getId() + ":" + picker.getDateTextField().getId(), "2/29/2012");
		assertNull(picker.getModelObject());
		formTester.submit();
		assertNotNull(picker.getModelObject());
		
		assertEquals(0, getTester().getMessages(FeedbackMessage.ERROR).size());
	}

}
