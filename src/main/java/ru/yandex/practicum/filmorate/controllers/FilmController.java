package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Запрос на добавление нового фильма:{}", film);
        film.setId(++generatedId);
        films.put(film.getId(), film);
        log.info("Добавление нового фильма:{}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Запрос на обновление фильма:{}", film);
        if (!films.containsKey(film.getId())) {
            log.error("Переданный id фильма не найден:" + film.getId());
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновление фильма:{}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

}
