package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidLoginValidator implements ConstraintValidator<ValidLogin, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            return !s.contains(" ") && !s.isBlank();
        }
        return false;
    }
}
