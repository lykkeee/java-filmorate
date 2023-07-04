package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public final UserDbStorage userDbStorage;

    public User createUser(User user) {
        nameValidator(user);
        user.setUserName(user.getName());
        return userDbStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userDbStorage.getUser(user.getId()) == null) {
            log.error("Переданный id пользователя не найден:" + user.getId());
            throw new DataNotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        nameValidator(user);
        user.setUserName(user.getName());
        return  userDbStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public void deleteUser(int userId) {
        if (userDbStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        userDbStorage.deleteUser(userId);
    }

    public User getUser(int userId) {
        if (userDbStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return userDbStorage.getUser(userId);
    }

    public void addFriend(Integer userId1, Integer userId2) {
        checkUsersExistence(userId1, userId2);
        if (userDbStorage.getUser(userId1).getFriendsList() == null) {
            userDbStorage.getUser(userId1).setFriendsList(new HashSet<>());
        }
        if (userDbStorage.getUser(userId2).getFriendsList() == null) {
            userDbStorage.getUser(userId2).setFriendsList(new HashSet<>());
        }
        userDbStorage.addFriend(userId1, userId2);
    }

    public void deleteFriend(Integer userId1, Integer userId2) {
        checkUsersExistence(userId1, userId2);
        if (userDbStorage.getUser(userId1) == null) {
            log.error("У пользователя с id " + userId1 + " список друзей пуст");
            throw new DataNotFoundException("У пользователя с id " + userId1 + " список друзей пуст");
        }
        userDbStorage.deleteFriend(userId1, userId2);
    }

    public List<User> getFriends(Integer userId) {
        if (userDbStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (userDbStorage.getUser(userId).getFriendsList() == null) {
            userDbStorage.getUser(userId).setFriendsList(new HashSet<>());
        }
        return userDbStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId1, Integer userId2) {
        checkUsersExistence(userId1, userId2);
        return userDbStorage.getCommonFriends(userId1, userId2);
    }

    private void nameValidator(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не было указано, подставлен логин:{}", user.getName());
        }
    }

    private void checkUsersExistence(Integer userId1, Integer userId2) {
        if (userDbStorage.getUser(userId1) == null) {
            log.error("Переданный id пользователя не найден:" + userId1);
            throw new DataNotFoundException("Пользователь с id = " + userId1 + " не найден");
        }
        if (userDbStorage.getUser(userId2) == null) {
            log.error("Переданный id пользователя не найден:" + userId2);
            throw new DataNotFoundException("Пользователь с id = " + userId2 + " не найден");
        }
    }

}
