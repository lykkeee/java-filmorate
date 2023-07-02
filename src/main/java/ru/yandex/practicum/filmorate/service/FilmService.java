package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final InMemoryFilmStorage inMemoryFilmStorage;
    public final InMemoryUserStorage inMemoryUserStorage;

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (inMemoryFilmStorage.getFilm(film.getId()) == null) {
            log.error("Переданный id фильма не найден:" + film.getId());
            throw new DataNotFoundException("Переданный id фильма не найден:" + film.getId());
        }
        return inMemoryFilmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public void deleteFilm(int filmId) {
        if (inMemoryFilmStorage.getFilm(filmId) == null) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        inMemoryFilmStorage.deleteFilm(filmId);
    }

    public Film getFilm(int filmId) {
        if (inMemoryFilmStorage.getFilm(filmId) == null) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return inMemoryFilmStorage.getFilm(filmId);
    }

    public void addLike(Integer userId, Integer filmId) {
        checkUserAndFilmExistence(userId, filmId);
        if (inMemoryFilmStorage.getFilm(filmId).getLikesList() == null) {
            inMemoryFilmStorage.getFilm(filmId).setLikesList(new HashSet<>());
        }
        inMemoryFilmStorage.getFilm(filmId).getLikesList().add(Long.valueOf(userId));
    }

    public void deleteLike(Integer userId, Integer filmId) {
        checkUserAndFilmExistence(userId, filmId);
        if (inMemoryFilmStorage.getFilm(filmId).getLikesList() == null) {
            log.info("К фильму с id " + filmId + "лайки отсутствуют");
            throw new DataNotFoundException("К фильму с id " + filmId + "лайки отсутствуют");
        }
        inMemoryFilmStorage.getFilm(filmId).getLikesList().remove(Long.valueOf(userId));
    }

    public List<Film> getTopFilms(Integer count) {
        int count1;
        count1 = Objects.requireNonNullElse(count, 10);
        int likes = 0;
        List<Film> topFilms = new ArrayList<>();
        for (Film film : inMemoryFilmStorage.getAllFilms()) {
            if (topFilms.size() < count1) {
                topFilms.add(film);
                topFilms.sort(Comparator.comparingInt(Film::getLikesListSize).reversed());
            } else {
                likes = topFilms.get(topFilms.size() - 1).getLikesListSize();
                if (film.getLikesListSize() > likes) {
                    topFilms.remove(topFilms.size() - 1);
                    topFilms.add(film);
                }
            }
        }
        return topFilms;
    }

    private void checkUserAndFilmExistence(Integer userId, Integer filmId) {
        if (inMemoryFilmStorage.getFilm(filmId) == null) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (inMemoryUserStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }
}
