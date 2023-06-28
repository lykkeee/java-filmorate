package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final InMemoryUserStorage inMemoryUserStorage;

    public User createUser(User user) {
        nameValidator(user);
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (inMemoryUserStorage.getUser(user.getId()) == null) {
            log.error("Переданный id пользователя не найден:" + user.getId());
            throw new DataNotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        nameValidator(user);
        return inMemoryUserStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void deleteUser(int userId) {
        if (inMemoryUserStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        inMemoryUserStorage.getUser(userId);
    }

    public User getUser(int userId) {
        if (inMemoryUserStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return inMemoryUserStorage.getUser(userId);
    }

    public void addFriend(Integer userId1, Integer userId2) {
        checkUsersExistence(userId1, userId2);
        if (inMemoryUserStorage.getUser(userId1).getFriendsList() == null) {
            inMemoryUserStorage.getUser(userId1).setFriendsList(new HashSet<>());
        }
        if (inMemoryUserStorage.getUser(userId2).getFriendsList() == null) {
            inMemoryUserStorage.getUser(userId2).setFriendsList(new HashSet<>());
        }
        inMemoryUserStorage.getUser(userId1).getFriendsList().add(Long.valueOf(userId2));
        inMemoryUserStorage.getUser(userId2).getFriendsList().add(Long.valueOf(userId1));
    }

    public void deleteFriend(Integer userId1, Integer userId2) {
        checkUsersExistence(userId1, userId2);
        if (inMemoryUserStorage.getUser(userId1).getFriendsList() == null) {
            log.error("У пользователя с id " + userId1 + " список друзей пуст");
            throw new DataNotFoundException("У пользователя с id " + userId2 + " список друзей пуст");
        }
        if (inMemoryUserStorage.getUser(userId2).getFriendsList() == null) {
            log.error("У пользователя с id " + userId2 + " список друзей пуст");
            throw new DataNotFoundException("У пользователя с id " + userId2 + " список друзей пуст");
        }
        inMemoryUserStorage.getUser(userId1).getFriendsList().remove(Long.valueOf(userId2));
        inMemoryUserStorage.getUser(userId2).getFriendsList().remove(Long.valueOf(userId1));
    }

    public List<User> getFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        if (inMemoryUserStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (inMemoryUserStorage.getUser(userId).getFriendsList() == null) {
            inMemoryUserStorage.getUser(userId).setFriendsList(new HashSet<>());
        }
        for (Long userFriend : inMemoryUserStorage.getUser(userId).getFriendsList()) {
            friends.add(inMemoryUserStorage.getUser((userFriend.intValue())));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer userId1, Integer userId2) {
        checkUsersExistence(userId1, userId2);
        List<User> commonFriends = getFriends(userId1);
        commonFriends.retainAll(getFriends(userId2));
        return commonFriends;
    }

    private void nameValidator(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не было указано, подставлен логин:{}", user.getName());
        }
    }

    private void checkUsersExistence(Integer userId1, Integer userId2) {
        if (inMemoryUserStorage.getUser(userId1) == null) {
            log.error("Переданный id пользователя не найден:" + userId1);
            throw new DataNotFoundException("Пользователь с id = " + userId1 + " не найден");
        }
        if (inMemoryUserStorage.getUser(userId2) == null) {
            log.error("Переданный id пользователя не найден:" + userId2);
            throw new DataNotFoundException("Пользователь с id = " + userId2 + " не найден");
        }
    }

}
