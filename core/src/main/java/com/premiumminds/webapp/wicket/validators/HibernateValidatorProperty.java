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

import java.util.HashMap;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class HibernateValidatorProperty implements IValidator<Object> {
	private static final long serialVersionUID = -4761422631335653016L;

	private IModel<?> beanModel;
	private String propertyName;

	public static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	
	public HibernateValidatorProperty(IModel<?> beanModel, String propertyName){
		this.beanModel = beanModel;
		this.propertyName = propertyName;
	}
	
	public void validate(IValidatable<Object> validatable) {
		Validator validator = HibernateValidatorProperty.validatorFactory.getValidator();
		
		@SuppressWarnings("unchecked")
		Set<ConstraintViolation<Object>> violations = validator.validateValue((Class<Object>)beanModel.getObject().getClass(), propertyName, validatable.getValue());
		
		if(!violations.isEmpty()){
			for(ConstraintViolation<?> violation : violations){
				ValidationError error = new ValidationError(violation.getMessage());
				
				String key = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
				if(getValidatorPrefix()!=null) key = getValidatorPrefix()+"."+key;
				
				error.addKey(key);
				error.setVariables(new HashMap<String, Object>(violation.getConstraintDescriptor().getAttributes()));
				
				//remove garbage from the attributes
				error.getVariables().remove("payload");
				error.getVariables().remove("message");
				error.getVariables().remove("groups");
				
				validatable.error(error);
			}
		}
	}
	
	protected String getValidatorPrefix(){
		return null;
	}
	
}
