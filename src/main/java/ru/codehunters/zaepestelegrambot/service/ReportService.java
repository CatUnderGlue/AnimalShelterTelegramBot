package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;

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
     * @exception NotFoundException Если в базе нет отчёта с указанным id
     */
    Report getById(Long id);

    /**
     * Получение отчёта из бд по дате
     * @param date Дата создания отчёта
     * @param id id Испытательного срока
     * @return Отчёт
     * @exception NotFoundException Если в базе нет отчёта с указанным id и датой
     */
    Report getByDateAndTrialId(LocalDate date, Long id);

    /**
     * Получение всех отчётов
     * @return Список всех отчётов
     * @exception NotFoundException Если база с отчётами пустая
     */
    List<Report> getAll();

    /**
     * Получение всех отчётов по id испытательного срока
     * @param id id испытательного срока из бд
     * @return Список отчётов по испытательному сроку
     * @exception NotFoundException Если в базе нет отчётов по указанному id испытательного срока
     */
    List<Report> gelAllByTrialPeriodId(Long id);

    /**
     * Изменение существующего отчёта
     * @param report Отчёт
     * @return Изменённый отчёт
     * @exception NotFoundException Если у передаваемого отчёта нет id или в базе нет отчёта с указанным id
     */
    Report update(Report report);

    /**
     * Удаление полученного из бд отчёта
     * @param report Отчёт, который уже есть в бд
     * @exception NotFoundException Если в базе нет отчёта с указанным id
     */
    void delete(Report report);

    /**
     * Удаление отчёта по id
     * @param id id отчёта
     * @exception NotFoundException Если в базе нет отчёта с указанным id
     */
    void deleteById(Long id);
}
