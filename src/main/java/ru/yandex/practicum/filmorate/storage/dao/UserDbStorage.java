package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        user.setId((Integer) simpleJdbcInsert.executeAndReturnKey(parameterSource));
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("update users set email = ?, user_name = ?, login = ?, birthday = ? where user_id = ?",
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from users", userRowMapper());
    }

    @Override
    public void deleteUser(int userId) {
        jdbcTemplate.update("delete from users where user_id = ?", userId);
    }

    public User getUser(int userId) {
        List<User> users = jdbcTemplate.query("select * from users where user_id = ?", userRowMapper(), userId);
        if (users.size() != 1) {
            throw new DataNotFoundException("Пользователь с таким id не найден: " + userId);
        }
        return users.get(0);
    }

    public void addFriend(Integer userId, Integer userId2) {
        jdbcTemplate.update("insert into friends (user_id, friend_id) values (?, ?)", userId, userId2);
    }

    public void deleteFriend(Integer userId, Integer userId2) {
        jdbcTemplate.update("delete from friends where user_id = ? and friend_id =?", userId, userId2);
    }

    public List<User> getFriends(Integer userId) {
        return jdbcTemplate.query("select * from users where user_id in (select friend_id from friends " +
                "where user_id = ?)", userRowMapper(), userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer userId2) {
        return jdbcTemplate.query("select * from users where user_id in (select friend_id from friends " +
                "where user_id = ?) and user_id in (select friend_id from friends " +
                "where user_id = ?)", userRowMapper(), userId, userId2);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        );
    }
}
