package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(final String message) {
        super(message);
    }
}