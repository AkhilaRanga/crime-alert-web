package com.crimealert.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidatorUtil {
	
	
	public static <T> String validateForm(T formObj)
	{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<T>> violations = validator.validate(formObj);
		
		String validateResponse = new String();
		
		for (ConstraintViolation<T> violation : violations) {
	    validateResponse += violation.getMessage();
	    validateResponse += "\n";
		}
		
		return validateResponse;
	}

}
