package com.premiumminds.webapp.wicket.test.validators;

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
	public void testInvalidNif() {
		IValidator<String> validator = new PortugueseNIFValidator();
		
		Validatable<String> validatable = new Validatable<String>("124456789");
		validator.validate(validatable);
		
		assertEquals(1, validatable.getErrors().size());
	}



}
