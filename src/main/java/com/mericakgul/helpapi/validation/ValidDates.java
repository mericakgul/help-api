package com.mericakgul.helpapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=DateValidator.class)
public @interface ValidDates {
    String message() default "{message.id}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
// TODO Date validation for busyPeriodDto couldn't be completed, finish it.
