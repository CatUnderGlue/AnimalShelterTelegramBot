package ru.codehunters.zaepestelegrambot.exception;

public class TrialPeriodNotFoundException extends RuntimeException {

    public TrialPeriodNotFoundException() {
        super("Испытательный срок не найден!");
    }
}