package ru.codehunters.zaepestelegrambot.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Пользователь не найден!");
    }
}
