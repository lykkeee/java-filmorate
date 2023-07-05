package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {
    private final UserService userService;

    private final User user = new User(1, "po@dkl.er", "login", "name", LocalDate.of(1990, 1, 1));


    @Test
    public void testGetUser() {
        userService.createUser(user);
        assertNotNull(userService.getUser(1));
    }

    @Test
    public void testDeleteUser() {
        userService.createUser(user);
        userService.deleteUser(1);
        Throwable thrown = Assertions.assertThrows(
                DataNotFoundException.class,
                () -> userService.getUser(1)
        );
        assertTrue(thrown.getMessage().contains("Пользователь с таким id не найден: 1"));
    }

    @Test
    public void testUpdateUser() {
        userService.createUser(user);
        User user1 = userService.getUser(1);
        userService.updateUser(new User(1, "po@dkl.er", "login", "", LocalDate.of(1990, 1, 1)));
        assertNotEquals(user1, userService.getUser(1));
    }

}
