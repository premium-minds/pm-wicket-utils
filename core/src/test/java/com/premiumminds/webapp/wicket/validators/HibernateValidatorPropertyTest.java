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

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.Validatable;
import org.apache.wicket.validation.ValidationError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HibernateValidatorPropertyTest {

	@Test
	public void testNotNull() {
		IValidator<Object> validator = new HibernateValidatorProperty(new Model<TestBean>(new TestBean("aaa", "aaa")), "a");
		
		Validatable<Object> validatable = new Validatable<Object>(null);
		validator.validate(validatable);
		
		assertEquals(1, validatable.getErrors().size());
		assertEquals("NotNull", getError(validatable).getKeys().get(0));
	}
	
	@Test
	public void testSuccess(){
		IValidator<Object> validator = new HibernateValidatorProperty(new Model<TestBean>(new TestBean("aaa", "aaa")), "a");
		
		Validatable<Object> validatable = new Validatable<Object>("bb");
		validator.validate(validatable);
		
		assertEquals(0, validatable.getErrors().size());
	}
	
	@Test
	public void testMessageParameters(){
		IValidator<Object> validator = new HibernateValidatorProperty(new Model<TestBean>(new TestBean("aaa", "aaa")), "b");
		
		Validatable<Object> validatable = new Validatable<Object>("a");
		validator.validate(validatable);
		
		assertEquals(1, validatable.getErrors().size());
		assertEquals("Size", getError(validatable).getKeys().get(0));
		
		assertEquals(2, getError(validatable).getVariables().size());
		assertEquals(2, getError(validatable).getVariables().get("min"));
		assertEquals(4, getError(validatable).getVariables().get("max"));
	}
	
	private ValidationError getError(Validatable<?> validatable)
	{
		return (ValidationError)validatable.getErrors().get(0);
	}	
	
	public class TestBean implements Serializable {
		private static final long serialVersionUID = -6112030970184764376L;

		@NotNull
		private String a;
		
		@Size(min=2, max=4)
		private String b;
		
		public TestBean(String a, String b){
			this.a = a;
			this.b = b;
		}

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}
	}

}
