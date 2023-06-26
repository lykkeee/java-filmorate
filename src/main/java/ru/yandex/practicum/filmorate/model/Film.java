package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.IsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @IsAfter("1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private long duration;
    private Set<Long> likesList;

    public int getLikesListSize() {
        if (likesList == null) {
            return 0;
        }
        return likesList.size();
    }
}
