package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class IsAfterValidator implements ConstraintValidator<IsAfter, LocalDate> {
    private LocalDate isAfterDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        isAfterDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate != null) {
            return localDate.isAfter(isAfterDate);
        } else {
            return false;
        }
    }
}
