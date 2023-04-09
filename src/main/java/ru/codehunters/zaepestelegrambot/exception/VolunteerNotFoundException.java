package ru.codehunters.zaepestelegrambot.exception;

public class VolunteerNotFoundException extends RuntimeException {

    public VolunteerNotFoundException() {
        super("Волонтёр не найден!");
    }
}