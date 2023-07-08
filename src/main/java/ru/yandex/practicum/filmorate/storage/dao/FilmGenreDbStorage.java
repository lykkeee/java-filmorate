package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class FilmGenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public List<Genre> getGenresByFilmId(int filmId) {
        return jdbcTemplate.query("select * from film_genre fg join genre g on g.id = fg.genre_id where film_id = ?", genreRowMapper(), filmId);
    }

    public void addGenreToFilm(int filmId, int genreId) {
        jdbcTemplate.update("insert into film_genre(film_id, genre_id) values(?, ?)", filmId, genreId);
    }

    public void deleteGeneresFromFilm(int filmId) {
        jdbcTemplate.update("delete from film_genre where film_id = ?", filmId);
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
