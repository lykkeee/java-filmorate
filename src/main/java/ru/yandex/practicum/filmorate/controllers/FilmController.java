package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") int filmId) {
        filmService.deleteFilm(filmId);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        return filmService.getFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @ResponseStatus(
            value = HttpStatus.NOT_FOUND,
            reason = "Переданный id фильма не найден")
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException(NotFoundException e) {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException e) {
    }
}
