package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Запрос на добавление нового фильма:{}", film);
        Film savedFilm = filmService.createFilm(film);
        log.info("Фильм добавлен {}", savedFilm);
        return savedFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Запрос на обновление фильма:{}", film);
        Film savedFilm = filmService.updateFilm(film);
        log.info("Фильм обновлен {}", savedFilm);
        return savedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрос на получение списка всех фильмов");
        List<Film> filmsList = filmService.getAllFilms();
        log.info("Список всех фильмов получен");
        return filmsList;
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") int filmId) {
        log.info("Запрос на удаление фильма c id:{}", filmId);
        filmService.deleteFilm(filmId);
        log.info("Фильм c id: " + filmId + " удален");
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        log.info("Запрос на получение фильма c id:{}", filmId);
        Film film = filmService.getFilm(filmId);
        log.info("Фильм получен:{}", film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Запрос на добавление лайка пользователя с id " + userId + " к фильму с id "
                + filmId);
        filmService.addLike(userId, filmId);
        log.info("Лайк пользователя с id " + userId + " добавлен к фильму с id "
                + filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Запрос на удаление лайка пользователя с id " + userId + " к фильму с id "
                + filmId);
        filmService.deleteLike(userId, filmId);
        log.info("Лайк пользователя с id " + userId + " к фильму с id "
                + filmId + " удален");
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        log.info("Запрос на получение списка фильмов по наибольшему количеству лайков");
        List<Film> topFilms = filmService.getTopFilms(count);
        log.info("Список фильмов по наибольшему количеству лайков получен");
        return topFilms;
    }

}
