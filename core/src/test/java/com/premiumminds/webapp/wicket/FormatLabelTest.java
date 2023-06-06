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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormatLabelTest extends AbstractComponentTest {
	private double tau = 6.28318530717958647692;
	private double e = 2.71828182845904523536;

	@Test
	public void testInitialization() {
		FormatLabel<Double> fl = new FormatLabel<Double>("id", Model.of(tau));
		getTester().getSession().setLocale(Locale.FRENCH);
		startTest(fl);

		getTester().assertModelValue(fl.getPageRelativePath(), tau);
		getTester().assertLabel(fl.getPageRelativePath(), "6,283 ºC");
	}

	@Test
	public void testNullModel() {
		FormatLabel<Double> fl = new FormatLabel<Double>("id");
		startTest(fl);

		assertNull(fl.getDefaultModel());
		getTester().assertModelValue(fl.getPageRelativePath(), null);
		getTester().assertLabel(fl.getPageRelativePath(), "");
	}

	@Test
	public void testConverterIsReadOnly() {
		FormatLabel<Double> fl = new FormatLabel<Double>("id");
		startTest(fl);

		assertThrows(ConversionException.class, () -> {
			fl.getConverter(Double.class).convertToObject("", fl.getLocale());
		});
	}

	@Test
	public void testConverterFailsForNullModel() {
		FormatLabel<Double> fl = new FormatLabel<Double>("id");
		startTest(fl);

		assertThrows(RuntimeException.class, () -> {
			fl.getConverter(Double.class).convertToString(null, fl.getLocale());
		});
	}

	@Test
	public void testIGenericComponentImplementation() {
		IModel<Double> model = new Model<Double>(tau);
		FormatLabel<Double> fl = new FormatLabel<Double>("id");
		assertNull(fl.getModel());
		assertNull(fl.getModelObject());

		fl.setModel(model);
		assertEquals(model, fl.getModel());
		assertEquals((Double)tau,  fl.getModelObject());

		fl.setModelObject(e);
		assertEquals((Double)e, fl.getModelObject());
		assertEquals(model, fl.getModel());
	}
}
