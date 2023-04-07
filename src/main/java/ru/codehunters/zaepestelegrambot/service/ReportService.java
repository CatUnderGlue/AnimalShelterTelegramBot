package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    /**
     * Сохранение отчёта в бд (Он же отвечает за обновление уже существующего отчёта)
     * @param report Отчёт для сохранения в бд
     * @return id отчёта
     */
    Long create(Report report);

    /**
     * Получение отчёта из бд по id
     * @param id id отчёта
     * @return Отчёт
     */
    Report getById(Long id);

    /**
     * Получение отчёта из бд по дате
     * @param date Дата создания отчёта
     * @param id id Испытательного срока
     * @return Отчёт
     */
    Report getByDateAndTrialId(LocalDate date, Long id);

    /**
     * @return Список всех отчётов
     */
    List<Report> getAll();

    /**
     * @param id id испытательного срока из бд
     * @return Список отчётов по испытательному сроку
     */
    List<Report> gelAllByTrialPeriodId(Long id);

    /**
     * Удаление полученного из бд отчёта
     * @param report Полученный из бд отчёт
     */
    void delete(Report report);

    /**
     * Удаление отчёта по id
     * @param id id отчёта
     */
    void deleteById(Long id);
}
