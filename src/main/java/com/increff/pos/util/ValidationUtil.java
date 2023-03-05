package com.increff.pos.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtil {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static <T> Set<ConstraintViolation<T>> validate(T t) {
        return factory.getValidator().validate(t);
    }
}
