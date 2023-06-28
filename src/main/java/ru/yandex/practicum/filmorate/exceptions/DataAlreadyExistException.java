package ru.yandex.practicum.filmorate.exceptions;

public class DataAlreadyExistException extends RuntimeException {
    public DataAlreadyExistException() {
    }

    public DataAlreadyExistException(final String message) {
        super(message);
    }
}
