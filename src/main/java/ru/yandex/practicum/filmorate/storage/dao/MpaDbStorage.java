package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("select * from film_mpa", mpaRowMapper());
    }

    public Mpa getMpa(int mpaId) {
        List<Mpa> mpas = jdbcTemplate.query("select * from film_mpa where id = ?", mpaRowMapper(), mpaId);
        if (mpas.size() != 1) {
            throw new DataNotFoundException("Рейтинг с таким id не найден: " + mpaId);
        }
        return mpas.get(0);
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
