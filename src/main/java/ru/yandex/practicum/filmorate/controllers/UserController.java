package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Запрос на добавление нового пользователя:{}", user);
        User savedUser = userService.createUser(user);
        log.info("Пользователь добавлен:{}", savedUser);
        return savedUser;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Запрос на обновление пользователя:{}", user);
        User savedUser = userService.updateUser(user);
        log.info("Пользователь обновлен:{}", savedUser);
        return savedUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        List<User> usersList = userService.getAllUsers();
        log.info("Список всех пользователей получен");
        return usersList;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        log.info("Запрос на удаление пользователя c id:{}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь c id: " + userId + " удален");
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int userId) {
        log.info("Запрос на получение пользователя c id:{}", userId);
        User user = userService.getUser(userId);
        log.info("Пользователь получен:{}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId1, @PathVariable("friendId") int userId2) {
        log.info("Запрос на добавление пользователя с id " + userId1 + " в друзья к пользователю с id "
                + userId2);
        userService.addFriend(userId1, userId2);
        log.info("Пользователь с id " + userId1 + " добавлен в друзья к пользователю с id "
                + userId2);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId1, @PathVariable("friendId") int userId2) {
        log.info("Запрос на удаление пользователя с id " + userId1 + " из друзей пользователя с id "
                + userId2);
        userService.deleteFriend(userId1, userId2);
        log.info("Пользователь с id " + userId1 + " удалён из друзей пользователя с id "
                + userId2);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") int userId) {
        log.info("Запрос на получение списка друзей пользователя с id:{}", userId);
        List<User> friendsList = userService.getFriends(userId);
        log.info("Список друзей пользователя с id " + userId + " получен");
        return friendsList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId1, @PathVariable("otherId") int userId2) {
        log.info("Запрос на получение списка общих друзей пользователя с id " + userId1 + " и пользователя с id "
                + userId2);
        List<User> commonFriends = userService.getCommonFriends(userId1, userId2);
        log.info("Список общих друзей пользователя с id " + userId1 + " и пользователя с id "
                + userId2 + " получен");
        return commonFriends;
    }

}
