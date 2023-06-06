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

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;



/**
 * Class the validates portuguese NIF numbers
 * Supports empty strings
 * 
 * @author acamilo
 *
 */
public class PortugueseNIFValidator extends StringValidator {
	private static final long serialVersionUID = -8262152957585701745L;

	@Override
	public void validate(IValidatable<String> validatable) {
		super.validate(validatable);
		
		final String value = validatable.getValue();
		
		if(value==null || value.length()==0) return;
		
		try {
			if(!isNIFValid(value)) validatable.error(new ValidationError(this));
		} catch(NumberFormatException e){
			validatable.error(new ValidationError(this));
		}
	}

	public static boolean isNIFValid(String number){
		// 9 digits required
		if(number.length() != 9) {
			return false;
		}

		boolean validFirstDigit = "123568".chars().mapToObj(c -> number.charAt(0) == c).filter(b -> b).findAny().orElse(false);

		List<String> firstDoubleDigits = Arrays.asList("45", "70", "71", "72", "74", "75", "77", "79", "90", "91", "98", "99");
		boolean validDoubleDigits = firstDoubleDigits.stream().map(c -> number.substring(0,  2).equals(c)).filter(b -> b).findAny().orElse(false);
		
		if(!validFirstDigit && !validDoubleDigits){
			return false;
		}

		int[] numbers = new int[9];
		char[] chars = number.toCharArray();
		for(int i = 0; i < 9; i++) {
			numbers[i] = Integer.parseInt(Character.toString(chars[i]));
		}

		// The weighted sum of all digits, including the control digit, should be multiple of 11
		int result = 0;
		for(int i = 0, j = 9; i < 8; i++, j--) {
			result += (j*numbers[i]);
		}

		// The infamous bug:
		// When the weighted sum of all digits, excluding the control digit,
		// equals 1 module 11, the control digit should be 10 (sic).
		// Then, we replace 10 by 0:
		if (result % 11 == 1) {
			return numbers[8] == 0;
		}

		return (result + numbers[8]) % 11 == 0;
	}
}
