package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAllMpa() {
        log.info("Запрос на получение списка всех рейтингов");
        List<Mpa> mpasList = mpaService.getAllMpa();
        log.info("Список всех рейтингов получен");
        return mpasList;
    }

    @GetMapping("{id}")
    public Mpa getMpa(@PathVariable("id") int mpaId) {
        log.info("Запрос на получение рейтинга c id:{}", mpaId);
        Mpa mpa = mpaService.getMpa(mpaId);
        log.info("Рейтинг получен:{}", mpaId);
        return mpa;
    }
}
