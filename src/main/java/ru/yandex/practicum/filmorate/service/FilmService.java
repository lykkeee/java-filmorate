package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    public Film createFilm(Film film) {
        log.info("Запрос на добавление нового фильма:{}", film);
        log.info("Добавление нового фильма:{}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Запрос на обновление фильма:{}", film);
        if (!filmStorage.getFilms().containsKey(film.getId())) {
            log.error("Переданный id фильма не найден:" + film.getId());
            throw new NotFoundException("Переданный id фильма не найден:" + film.getId());
        }
        log.info("Обновление фильма:{}", film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAllFilms();
    }

    public void deleteFilm(int filmId) {
        log.info("Запрос на удаление фильма с id:{}", filmId);
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        filmStorage.deleteFilm(filmId);
        log.info("Фильм удален:{}", filmStorage.getFilms().get(filmId));
    }

    public Film getFilm(int filmId) {
        log.info("Запрос на получение фильма с id:{}", filmId);
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        log.info("Фильм получен:{}", filmStorage.getFilms().get(filmId));
        return filmStorage.getFilms().get(filmId);
    }

    public void addLike(Integer userId, Integer filmId) {
        log.info("Запрос на добавление лайка пользователя с id " + userId + " к фильму с id "
                + filmId);
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (filmStorage.getFilms().get(filmId).getLikesList() == null) {
            filmStorage.getFilms().get(filmId).setLikesList(new HashSet<>());
        }
        filmStorage.getFilms().get(filmId).getLikesList().add(Long.valueOf(userId));
        log.info("Лайк пользователя с id " + userId + " добавлен к фильму с id "
                + filmId);
    }

    public void deleteLike(Integer userId, Integer filmId) {
        log.info("Запрос на удаление лайка пользователя с id " + userId + " к фильму с id "
                + filmId);
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (filmStorage.getFilms().get(filmId).getLikesList() == null) {
            log.info("К фильму с id " + filmId + "лайки отсутствуют");
            throw new NotFoundException("К фильму с id " + filmId + "лайки отсутствуют");
        }
        filmStorage.getFilms().get(filmId).getLikesList().remove(Long.valueOf(userId));
        log.info("Лайк пользователя с id " + userId + " к фильму с id "
                + filmId + " удален");
    }

    public List<Film> getTopFilms(Integer count) {
        log.info("Запрос на получение списка фильмов по наибольшему количеству лайков");
        int count1;
        count1 = Objects.requireNonNullElse(count, 10);
        int likes = 0;
        List<Film> topFilms = new ArrayList<>();
        for (Film film : filmStorage.getAllFilms()) {
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
}
