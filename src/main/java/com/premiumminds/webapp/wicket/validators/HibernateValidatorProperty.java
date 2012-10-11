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
