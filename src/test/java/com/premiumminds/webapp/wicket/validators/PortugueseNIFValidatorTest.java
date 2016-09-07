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
package com.premiumminds.webapp.wicket.validators;

import static org.junit.Assert.*;

import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.Validatable;

import org.junit.Test;

import com.premiumminds.webapp.wicket.validators.PortugueseNIFValidator;


public class PortugueseNIFValidatorTest {

	@Test
	public void testValidNif() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("241250609");
		validator.validate(validatable);
		
		assertEquals(0, validatable.getErrors().size());

	}
	
	@Test
	public void testValidNifUsual() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("123456789");
		validator.validate(validatable);
		
		assertEquals(0, validatable.getErrors().size());
	}
	
	
	@Test
	public void testValidNif0term() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("504426290");
		validator.validate(validatable);
		
		assertEquals(0, validatable.getErrors().size());
	}
	
	@Test
	public void testValidNif0TermWithMod1() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("504646680");
		validator.validate(validatable);
		
		assertEquals(0, validatable.getErrors().size());
	}
	
	@Test
	public void testInvalidNif() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("124456789");
		validator.validate(validatable);
		
		assertEquals(1, validatable.getErrors().size());
	}

	@Test
	public void testAnotherInvalidNif() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("505646780");
		validator.validate(validatable);
		
		assertEquals(1, validatable.getErrors().size());
	}

}
