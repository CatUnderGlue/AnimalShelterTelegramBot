package ru.codehunters.zaepestelegrambot.exception;

public class DogOwnerNotFoundException extends RuntimeException {

    public DogOwnerNotFoundException() {
        super("Хозяин собаки не найден!");
    }
}
