package com.increff.pos.util;

import com.increff.pos.exception.ApiException;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationUtil {
    public static <T> Set<ConstraintViolation<T>> validate(T form) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        return violations;
    }
    public static void validateDates(String startDate, String endDate) throws ApiException {
        if (startDate.compareTo(endDate) > 0)
            throw new ApiException("Start date cannot be greater than end date!");
    }

    public static <T> void checkValid(T obj) throws ApiException {
        Set<ConstraintViolation<T>> violations = validate(obj);
        if (violations.isEmpty()) {
            return;
        }
        List<String> errorList = new ArrayList<String>(violations.size());
        for (ConstraintViolation<T> violation : violations) {
            String error = new String("Error in field: ");
            error += violation.getPropertyPath().toString();
            error += ", Message: ";
            error += violation.getMessage();
            errorList.add(error);
        }
        throw new ApiException("Input validation failed", errorList);
    }

    public static <T> void checkValid(List<T> obj) throws ApiException {
        List<String> errorList = new ArrayList<>();
        for(Object temp : obj){
            Set<ConstraintViolation<T>> violations = validate((T)temp);
            for (ConstraintViolation<T> violation : violations) {
                StringBuilder error = new StringBuilder("Error in field: ");
                error.append(violation.getPropertyPath().toString());
                error.append(", Message: ");
                error.append(violation.getMessage());
                errorList.add(error.toString());
            }
        }
        if(errorList.isEmpty()){
            return;
        }
        throw new ApiException("Input validation failed", errorList);
    }

    public static <T> void checkValidMultiple(List<T> obj) throws ApiException{
        List<String> errorList = new ArrayList<>();
        int count = 1;
        for(Object temp : obj){
            count++;
            Set<ConstraintViolation<T>> violations = validate((T)temp);
            for (ConstraintViolation<T> violation : violations) {
                StringBuilder error = new StringBuilder("Error in field: ");
                error.append(violation.getPropertyPath().toString());
                error.append(", Message: ");
                error.append(violation.getMessage());
                error.append(" on line number: " +count);
                errorList.add(error.toString());
            }
        }
        if(errorList.isEmpty()){
            return;
        }
        throw new ApiException("Input validation failed", errorList);
    }
}
