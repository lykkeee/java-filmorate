package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmServiceTest {
    private final FilmService filmService;

    private final Film film = new Film(null, "name", "description", LocalDate.of(1990, 1, 1), 120L, new Mpa(1));


    @Test
    public void testGetFilm() {
        filmService.createFilm(film);
        assertNotNull(filmService.getFilm(1));
    }

    @Test
    public void testDeleteFilm() {
        filmService.createFilm(film);
        filmService.deleteFilm(1);
        Throwable thrown = Assertions.assertThrows(
                DataNotFoundException.class,
                () -> filmService.getFilm(1)
        );
        assertTrue(thrown.getMessage().contains("Фильм с таким id не найден: 1"));
    }

    @Test
    public void testUpdateFilm() {
        filmService.createFilm(film);
        Film film1 = filmService.getFilm(1);
        filmService.updateFilm(new Film(1, "name_updated", "description", LocalDate.of(1990, 1, 1), 120L, new Mpa(1)));
        assertNotEquals(film1, filmService.getFilm(1));
    }

}