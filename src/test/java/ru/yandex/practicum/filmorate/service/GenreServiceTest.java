package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreServiceTest {
    private final GenreService genreService;

    @Test
    public void testGetGenre() {
        assertEquals(genreService.getGenre(1), new Genre(1, "Комедия"));
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            genres.add(genreService.getGenre(i));
        }
        assertEquals(genreService.getAllGenres(), genres);
    }

}