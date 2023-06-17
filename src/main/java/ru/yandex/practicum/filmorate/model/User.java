package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.ValidLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @Email
    private final String email;
    @ValidLogin
    private final String login;
    private String name;
    @PastOrPresent
    private final LocalDate birthday;

    public void nameValidator(String name) {    //На такое можно как-то аннотацию сделать? А то я не нашел каким образом
        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
    }
}
