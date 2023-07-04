package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.HashSet;
import java.util.List;

@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(film);
        film.setId((Integer) simpleJdbcInsert.executeAndReturnKey(parameterSource));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("update films set film_name = ?, description = ?, release_date = ?, duration = ?, " +
                        "mpa_id = ? where film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query("select f.film_id, f.film_name, f.description, f.release_date, " +
                "f.duration, f.mpa_id, m.mpa_name from films f join film_mpa m on f.mpa_id=m.mpa_id " +
                "order by f.film_id", filmRowMapper());
    }

    @Override
    public void deleteFilm(int filmId) {
        jdbcTemplate.update("delete from films where film_id = ?", filmId);
    }

    public Film getFilm(int filmId) {
        List<Film> films = jdbcTemplate.query("select f.film_id, f.film_name, f.description, f.release_date, " +
                "f.duration, f.mpa_id, m.mpa_name from films f left join film_mpa m on f.mpa_id=m.mpa_id " +
                "where film_id = ?", filmRowMapper(), filmId);
        if (films.size() != 1) {
            throw new DataNotFoundException("Фильм с таким id не найден: " + filmId);
        }
        return films.get(0);
    }

    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("insert into film_likes (film_id, user_id) values (?, ?)", filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("delete from film_likes where film_id = ? and user_id = ?", filmId, userId);
    }

    public List<Long> getLikesByFilm(Integer filmId) {
        return jdbcTemplate.query("select * from film_likes where film_id = ?", (rs, rowNum) ->
                rs.getLong("user_id"), filmId);
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("duration"),
                new Mpa(rs.getInt("mpa_id")),
                new HashSet<>()
        );
    }
}
