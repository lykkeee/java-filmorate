package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createEmptyFilm() {
        Film film = filmController.createFilm(new Film(null, null, null, null, 0, null));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongNameFilm() {
        Film film = filmController.createFilm(new Film(null, "", "faa", LocalDate.of(1950, 12, 4), 120, null));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDescriptionFilm() {
        Film film = filmController.createFilm(new Film(null, "DD", "d".repeat(201), LocalDate.of(1950, 12, 4), 120, null));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDateFilm() {
        Film film = filmController.createFilm(new Film(null, "DD", "faa", LocalDate.of(1850, 12, 4), 120, null));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDurationFilm() {
        Film film = filmController.createFilm(new Film(null, "DD", "faa", LocalDate.of(1950, 12, 4), -120, null));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

}