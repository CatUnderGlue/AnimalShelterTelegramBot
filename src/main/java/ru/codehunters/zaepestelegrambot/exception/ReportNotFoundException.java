package ru.codehunters.zaepestelegrambot.exception;

public class ReportNotFoundException extends RuntimeException {

    public ReportNotFoundException() {
        super("Отчёт не найден!");
    }
}