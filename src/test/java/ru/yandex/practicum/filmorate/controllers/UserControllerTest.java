package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createEmptyUser() {
        User user = userController.createUser(new User(null, null, null, null, null));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongEmailUser() {
        User user = userController.createUser(new User(0, "wrongEmail", "login", "", LocalDate.of(1988, 1, 1)));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongLoginUser() {
        User user = userController.createUser(new User(0, "email@kl.ee", " ", "", LocalDate.of(1988, 1, 1)));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDateUser() {
        User user = userController.createUser(new User(0, "email@kl.ee", "login", "", LocalDate.of(2025, 1, 1)));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

}