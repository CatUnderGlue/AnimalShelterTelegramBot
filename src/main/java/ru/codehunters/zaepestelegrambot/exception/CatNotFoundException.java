package ru.codehunters.zaepestelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CatNotFoundException extends RuntimeException {

    public CatNotFoundException() {
        super("Кот не найден!");
    }
}