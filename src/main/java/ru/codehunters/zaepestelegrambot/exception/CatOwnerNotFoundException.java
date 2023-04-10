package ru.codehunters.zaepestelegrambot.exception;

public class CatOwnerNotFoundException extends RuntimeException {

    public CatOwnerNotFoundException() {
        super("Хозяин кота не найден!");
    }
}
