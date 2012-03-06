package com.premiumminds.webapp.wicket.validators;

import org.apache.wicket.validation.IValidatable;
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
	protected void onValidate(IValidatable<String> validatable) {
		final String value = validatable.getValue();
		
		if(value==null || value.length()==0) return;
		
		try {
			if(!isNIFValid(value)) error(validatable);
		} catch(NumberFormatException e){
			error(validatable);
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
        for(int i = 0, j = 9; i < 9; i++, j--) {
            result += (j*numbers[i]);
        }
       
        return (result%11) == 0.0f;		
	}
}
