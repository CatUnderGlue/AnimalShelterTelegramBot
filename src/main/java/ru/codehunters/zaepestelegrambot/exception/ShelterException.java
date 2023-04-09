package ru.codehunters.zaepestelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShelterException extends RuntimeException {
    public ShelterException(String message) {
        super(message);
    }
}
