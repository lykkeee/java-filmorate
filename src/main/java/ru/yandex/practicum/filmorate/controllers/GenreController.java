package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Запрос на получение списка всех жанров");
        List<Genre> genresList = genreService.getAllGenres();
        log.info("Список всех жанров получен");
        return genresList;
    }

    @GetMapping("{id}")
    public Genre getGenre(@PathVariable("id") int genreId) {
        log.info("Запрос на получение жанра c id:{}", genreId);
        Genre genre = genreService.getGenre(genreId);
        log.info("Жанр получен:{}", genreId);
        return genre;
    }
}
