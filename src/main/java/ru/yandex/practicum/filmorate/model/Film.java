package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.IsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @JsonIgnore
    private String filmName;
    @Size(max = 200)
    private String description;
    @IsAfter("1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private long duration;
    @JsonIgnore
    private Set<Long> likesList;
    private Mpa mpa;
    @JsonIgnore
    private int mpaId;
    private Set<Genre> genres;

    public Film(Integer id, String name, String description, LocalDate releaseDate, long duration, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    @JsonIgnore
    public int getLikesListSize() {
        if (likesList == null) {
            return 0;
        }
        return likesList.size();
    }
}
