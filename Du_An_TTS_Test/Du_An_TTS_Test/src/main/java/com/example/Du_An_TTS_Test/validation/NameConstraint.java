package com.example.Du_An_TTS_Test.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface NameConstraint {

    String messange() default "Invalid username";

    int min();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
