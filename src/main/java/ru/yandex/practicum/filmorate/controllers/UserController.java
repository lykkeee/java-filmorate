package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatedId = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Запрос на добавление нового пользователя:{}", user);
        user.setId(++generatedId);
        user.nameValidator(user.getName());
        users.put(user.getId(), user);
        log.info("Добавление нового пользователя:{}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Запрос на обновление пользователя:{}", user);
        if (!users.containsKey(user.getId())) {
            log.error("Переданный id фильма не найден:" + user.getId());
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
        user.nameValidator(user.getName());
        users.put(user.getId(), user);
        log.info("Обновление пользователя:{}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return new ArrayList<>(users.values());
    }

}
