package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from genre", genreRowMapper());
    }

    public Genre getGenre(int genreId) {
        List<Genre> genres = jdbcTemplate.query("select * from genre where genre_id = ?", genreRowMapper(), genreId);
        if (genres.size() != 1) {
            throw new DataNotFoundException("Жанр с таким id не найден: " + genreId);
        }
        return genres.get(0);
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        );
    }
}
