package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public void nameValidator(String name) {    //На такое можно как-то аннотацию сделать? А то я не нашел каким образом
        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
    }
}
