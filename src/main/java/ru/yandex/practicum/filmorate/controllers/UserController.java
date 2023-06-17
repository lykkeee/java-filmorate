package ru.yandex.practicum.filmorate.controllers;

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
    private int generatedId = 1;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        user.setId(generatedId++);
        user.nameValidator(user.getName());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            user.nameValidator(user.getName());
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

}
