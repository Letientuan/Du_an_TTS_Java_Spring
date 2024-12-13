package com.example.Du_An_TTS_Test.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class NameValidator implements ConstraintValidator<NameConstraint,String> {

    private int min;
    @Override
    public void initialize(NameConstraint constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(Objects.isNull(s))
            return true;
        return false;
    }
}
