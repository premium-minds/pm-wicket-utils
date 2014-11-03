/**
 * Copyright (C) 2014 Premium Minds.
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

	private boolean isNIFValid(String number){
		// 9 digits required
        if(number.length() != 9) return false;
        // start with 1, 2, 5, 6, 8 or 9
        if(!"125689".contains(Character.toString(number.charAt(0)))) return false;

        int[] numbers = new int[9];
        char[] chars = number.toCharArray();
        for(int i = 0; i < 9; i++) numbers[i] = Integer.parseInt(Character.toString(chars[i]));
       
        // mod 11
        float result = 0.0f;
        for(int i = 0, j = 9; i < 8; i++, j--) {
            result += (j*numbers[i]);
        }
        
        float verification = 11-(result%11);
        if (verification > 9) verification = verification%10; 
        
        return verification == numbers[8]  ;		
	}
}
