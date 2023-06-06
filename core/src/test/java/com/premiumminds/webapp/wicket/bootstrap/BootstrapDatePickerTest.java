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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.premiumminds.webapp.wicket.bootstrap.datepicker.BootstrapDatePickerBehaviour;
import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupFragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BootstrapDatePickerTest extends AbstractComponentTest {
	private class TestBootstrapDatePicker extends BootstrapDatePicker {
		private static final long serialVersionUID = 1L;

		private DateTextField field;

		public TestBootstrapDatePicker(String id) {
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

		public Component getInnerField() {
			return field;
		}
	}

	@SuppressWarnings("deprecation")
	private Date christmas = new Date(110, 11, 25);
	@SuppressWarnings("deprecation")
	private Date newYear = new Date(110, 0, 1);

	@Test
	public void testDatepickerInvokeSpecialDates() {
		final List<SpecialDate> list = Arrays.asList(new SpecialDate(christmas, "holiday", "Christmas"));

		BootstrapDatePicker picker = new BootstrapDatePicker("picker") {
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<SpecialDate> getSpecialDates() {
				return list;
			}
		};

		List<? extends Behavior> behaviors = picker.getBehaviors();
		assertEquals(1, behaviors.size());
		assertTrue(behaviors.get(0) instanceof BootstrapDatePickerBehaviour);
		assertEquals(list, ((BootstrapDatePickerBehaviour)behaviors.get(0)).getSpecialDates());
	}
	
	@Test
	public void testModel() {
		TestBootstrapDatePicker picker = new TestBootstrapDatePicker("picker");

		getTester().startComponentInForm(picker, "date");
		FormTester formTester = getTester().newFormTester("form");
		assertNull(picker.getModelObject());
		formTester.setValue(picker.getId() + ":" + picker.getDateTextField().getId(), "2/29/2012");
		assertNull(picker.getModelObject());
		formTester.submit();
		assertNotNull(picker.getModelObject());
		
		assertEquals(0, getTester().getMessages(FeedbackMessage.ERROR).size());
	}
	
	@Test
	public void testDatepickerLifecycle() {
		TestBootstrapDatePicker picker = new TestBootstrapDatePicker("picker");

		startTest(picker);

		getTester().assertComponent(picker.getPageRelativePath(), TestBootstrapDatePicker.class);
		getTester().assertComponent(picker.getInnerField().getPageRelativePath(), DateTextField.class);
		assertEquals(picker.getInnerField(), picker.getDateTextField());
	}

	@Test
	public void testLifecycleWithoutDateField() {
		Assertions.assertThrows(WicketRuntimeException.class, () -> {

			BootstrapDatePicker picker = new BootstrapDatePicker("picker");

			startTest(picker);
		});


	}

	@Test
	public void testIGenericComponentImplementation() {
		IModel<Date> model = new Model<Date>(christmas);
		BootstrapDatePicker picker = new TestBootstrapDatePicker("picker");

		startTest(picker);

		assertNotNull(picker.getModel());
		assertNull(picker.getModelObject());

		picker.setModel(model);
		assertEquals(model, picker.getModel());
		assertEquals(christmas,  picker.getModelObject());

		picker.setModelObject(newYear);
		assertEquals(newYear, picker.getModelObject());
		assertEquals(model, picker.getModel());
	}
}
