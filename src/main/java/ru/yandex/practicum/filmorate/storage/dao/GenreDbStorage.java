package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;


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
        List<Genre> genres = jdbcTemplate.query("select * from genre where id = ?", genreRowMapper(), genreId);
        if (genres.size() != 1) {
            throw new DataNotFoundException("Жанр с таким id не найден: " + genreId);
        }
        return genres.get(0);
    }

    public void load(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "select * from genre g, film_genre fg where fg.genre_id = g.id and fg.film_id in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.addGenre(new Genre(rs.getInt("genre.id"), rs.getString("genre.name")));
        }, films.stream().map(Film::getId).toArray());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
