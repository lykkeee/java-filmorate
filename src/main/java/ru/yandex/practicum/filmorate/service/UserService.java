package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final UserStorage userStorage;

    public User createUser(User user) {
        log.info("Запрос на добавление нового пользователя:{}", user);
        user.nameValidator(user.getName());
        log.info("Добавление нового пользователя:{}", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя:{}", user);
        if (!userStorage.getUsers().containsKey(user.getId())) {
            log.error("Переданный id пользователя не найден:" + user.getId());
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        user.nameValidator(user.getName());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    public void deleteUser(int userId) {
        log.info("Запрос на удаление пользователя с id:{}", userId);
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        userStorage.deleteUser(userId);
        log.info("Пользователь удален:{}", userStorage.getUsers().get(userId));
    }

    public User getUser(int userId) {
        log.info("Запрос на получение пользователя с id:{}", userId);
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        log.info("Пользователь получен:{}", userStorage.getUsers().get(userId));
        return userStorage.getUsers().get(userId);
    }

    public void addFriend(Integer userId1, Integer userId2) {
        log.info("Запрос на добавление пользователя с id " + userId1 + " в друзья к пользователю с id "
                + userId2);
        if (!userStorage.getUsers().containsKey(userId1)) {
            log.error("Переданный id пользователя не найден:" + userId1);
            throw new NotFoundException("Пользователь с id = " + userId1 + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId2)) {
            log.error("Переданный id пользователя не найден:" + userId2);
            throw new NotFoundException("Пользователь с id = " + userId2 + " не найден");
        }
        if (userStorage.getUsers().get(userId1).getFriendsList() == null) {
            userStorage.getUsers().get(userId1).setFriendsList(new HashSet<>());
        }
        if (userStorage.getUsers().get(userId2).getFriendsList() == null) {
            userStorage.getUsers().get(userId2).setFriendsList(new HashSet<>());
        }
        userStorage.getUsers().get(userId1).getFriendsList().add(Long.valueOf(userId2));
        userStorage.getUsers().get(userId2).getFriendsList().add(Long.valueOf(userId1));
        log.info("Пользователь с id " + userId1 + " добавлен в друзья к пользователю с id "
                + userId2);
    }

    public void deleteFriend(Integer userId1, Integer userId2) {
        log.info("Запрос на удаление пользователя с id " + userId1 + " из друзей пользователя с id "
                + userId2);
        if (!userStorage.getUsers().containsKey(userId1)) {
            log.error("Переданный id пользователя не найден:" + userId1);
            throw new NotFoundException("Пользователь с id = " + userId1 + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId2)) {
            log.error("Переданный id пользователя не найден:" + userId2);
            throw new NotFoundException("Пользователь с id = " + userId2 + " не найден");
        }
        if (userStorage.getUsers().get(userId1).getFriendsList() == null) {
            log.error("У пользователя с id " + userId1 + " список друзей пуст");
            throw new NotFoundException("У пользователя с id " + userId2 + " список друзей пуст");
        }
        if (userStorage.getUsers().get(userId2).getFriendsList() == null) {
            log.error("У пользователя с id " + userId2 + " список друзей пуст");
            throw new NotFoundException("У пользователя с id " + userId2 + " список друзей пуст");
        }
        userStorage.getUsers().get(userId1).getFriendsList().remove(Long.valueOf(userId2));
        userStorage.getUsers().get(userId2).getFriendsList().remove(Long.valueOf(userId1));
        log.info("Пользователь с id " + userId1 + " удалён из друзей пользователя с id "
                + userId2);
    }

    public List<User> getFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        log.info("Запрос на получение списка друзей пользователя с id:{}", userId);
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (userStorage.getUsers().get(userId).getFriendsList() == null) {
            userStorage.getUsers().get(userId).setFriendsList(new HashSet<>());
        }
        for (Long userFriend : userStorage.getUsers().get(userId).getFriendsList()) {
            friends.add(userStorage.getUsers().get(userFriend.intValue()));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer userId1, Integer userId2) {
        log.info("Запрос на получение списка общих друзей пользователя с id " + userId1 + " и пользователя с id "
                + userId2);
        if (!userStorage.getUsers().containsKey(userId1)) {
            log.error("Переданный id пользователя не найден:" + userId1);
            throw new NotFoundException("Пользователь с id = " + userId1 + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId2)) {
            log.error("Переданный id пользователя не найден:" + userId2);
            throw new NotFoundException("Пользователь с id = " + userId2 + " не найден");
        }
        List<User> commonFriends = getFriends(userId1);
        commonFriends.retainAll(getFriends(userId2));
        return commonFriends;
    }

}
