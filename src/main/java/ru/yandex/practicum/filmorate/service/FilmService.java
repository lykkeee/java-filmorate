package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final FilmDbStorage filmDbStorage;
    public final UserDbStorage userDbStorage;
    public final FilmGenreDbStorage filmGenreDbStorage;
    public final MpaDbStorage mpaDbStorage;

    public Film createFilm(Film film) {
        return setForFilm(film);
    }

    public Film updateFilm(Film film) {
        if (filmDbStorage.getFilm(film.getId()) == null) {
            log.error("Переданный id фильма не найден:" + film.getId());
            throw new DataNotFoundException("Переданный id фильма не найден:" + film.getId());
        }
        return setForFilm(film);
    }

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        if (filmDbStorage.getAllFilms() != null) {
            for (Film film : filmDbStorage.getAllFilms()) {
                film.setMpa(mpaDbStorage.getMpa(film.getMpa().getId()));
                film.setGenres(new HashSet<>(filmGenreDbStorage.getGenresByFilmId(film.getId())));
                films.add(film);
            }
        }
        return films;
    }

    public void deleteFilm(int filmId) {
        if (filmDbStorage.getFilm(filmId) == null) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        filmDbStorage.deleteFilm(filmId);
    }

    public Film getFilm(int filmId) {
        if (filmDbStorage.getFilm(filmId) == null) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        Film film = filmDbStorage.getFilm(filmId);
        film.setMpa(mpaDbStorage.getMpa(film.getMpa().getId()));
        film.setGenres(new HashSet<>(filmGenreDbStorage.getGenresByFilmId(film.getId())));
        return film;
    }

    public void addLike(Integer userId, Integer filmId) {
        checkUserAndFilmExistence(userId, filmId);
        filmDbStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer userId, Integer filmId) {
        checkUserAndFilmExistence(userId, filmId);
        if (filmDbStorage.getLikesByFilm(filmId) == null) {
            log.info("К фильму с id " + filmId + "лайки отсутствуют");
            throw new DataNotFoundException("К фильму с id " + filmId + " лайки отсутствуют");
        }
        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getTopFilms(Integer count) {
        int count1;
        count1 = Objects.requireNonNullElse(count, 10);
        int likes = 0;
        List<Film> topFilms = new ArrayList<>();
        for (Film film : filmDbStorage.getAllFilms()) {
            film.setLikesList(new HashSet<>(filmDbStorage.getLikesByFilm(film.getId())));
            if (topFilms.size() < count1) {
                topFilms.add(film);
                film.setMpa(mpaDbStorage.getMpa(film.getMpa().getId()));
                film.setGenres(new HashSet<>(filmGenreDbStorage.getGenresByFilmId(film.getId())));
                topFilms.sort(Comparator.comparingInt(Film::getLikesListSize).reversed());
            } else {
                likes = topFilms.get(topFilms.size() - 1).getLikesListSize();
                if (film.getLikesListSize() > likes) {
                    topFilms.remove(topFilms.size() - 1);
                    topFilms.add(film);
                    film.setMpa(mpaDbStorage.getMpa(film.getMpa().getId()));
                    film.setGenres(new HashSet<>(filmGenreDbStorage.getGenresByFilmId(film.getId())));
                }
            }
        }
        return topFilms;
    }

    private void checkUserAndFilmExistence(Integer userId, Integer filmId) {
        if (filmDbStorage.getFilm(filmId) == null) {
            log.error("Переданный id фильма не найден:" + filmId);
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (userDbStorage.getUser(userId) == null) {
            log.error("Переданный id пользователя не найден:" + userId);
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private Film setForFilm(Film film) {
        film.setFilmName(film.getName());
        film.setMpaId(film.getMpa().getId());
        Film film1 = film;
        if (film1.getId() == null) {
            film1 = filmDbStorage.createFilm(film);
        } else {
            film1 = filmDbStorage.updateFilm(film);
        }
        if (filmGenreDbStorage.getGenresByFilmId(film.getId()) !=  null) {
            filmGenreDbStorage.deleteGeneresFromFilm(film1.getId());
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                filmGenreDbStorage.addGenreToFilm(film1.getId(), genre.getId());
            }
        }
        film1.setGenres(new HashSet<>(filmGenreDbStorage.getGenresByFilmId(film1.getId())));
        return film1;
    }
}
