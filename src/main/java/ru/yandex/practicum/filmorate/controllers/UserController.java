package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);

    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId1, @PathVariable("friendId") int userId2) {
        userService.addFriend(userId1, userId2);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId1, @PathVariable("friendId") int userId2) {
        userService.deleteFriend(userId1, userId2);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId1, @PathVariable("otherId") int userId2) {
        return userService.getCommonFriends(userId1, userId2);
    }

    @ResponseStatus(
            value = HttpStatus.NOT_FOUND,
            reason = "Переданный id пользователя не найден")
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException(NotFoundException e) {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException e) {
    }
}
