package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.ReportRepo;
import ru.codehunters.zaepestelegrambot.service.impl.ReportServiceImpl;
import ru.codehunters.zaepestelegrambot.exception.AlreadyExistsException;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    /**
     * Сохранение отчёта в бд (Он же отвечает за обновление уже существующего отчёта)<br>
     * Используется метод репозитория {@link ReportRepo#save(Object)}
     * @param report Отчёт для сохранения в бд
     * @return Созданный отчёт
     */
    Report create(Report report);

    /**
     * Получение отчёта из бд по id<br>
     * Используется метод репозитория {@link ReportRepo#findById(Object)}
     * @param id id отчёта
     * @return Отчёт
     * @exception NotFoundException Если в базе нет отчёта с указанным id
     */
    Report getById(Long id);

    /**
     * Получение отчёта из бд по дате<br>
     * Используется метод репозитория {@link ReportRepo#findByReceiveDateAndTrialPeriodId(LocalDate, Long)}
     * @param date Дата создания отчёта
     * @param id id Испытательного срока
     * @return Отчёт
     * @exception NotFoundException Если в базе нет отчёта с указанным id и датой
     */
    Report getByDateAndTrialId(LocalDate date, Long id);

    /**
     * Получение всех отчётов<br>
     * Используется метод репозитория {@link ReportRepo#findAll()}
     * @return Список всех отчётов
     * @exception NotFoundException Если база с отчётами пустая
     */
    List<Report> getAll();

    /**
     * Получение всех отчётов по id испытательного срока<br>
     * Используется метод репозитория {@link ReportRepo#findAllByTrialPeriodId(Long)}
     * @param id id испытательного срока из бд
     * @return Список отчётов по испытательному сроку
     * @exception NotFoundException Если в базе нет отчётов по указанному id испытательного срока
     */
    List<Report> gelAllByTrialPeriodId(Long id);

    /**
     * Изменение существующего отчёта<br>
     * Используется метод этого же сервиса {@link ReportServiceImpl#getById(Long)}
     * @param report Отчёт
     * @return Изменённый отчёт
     * @exception NotFoundException Если у передаваемого отчёта нет id или в базе нет отчёта с указанным id
     */
    Report update(Report report);

    /**
     * Удаление полученного из бд отчёта<br>
     * Используется метод этого же сервиса {@link ReportServiceImpl#getById(Long)}
     * @param report Отчёт, который уже есть в бд
     * @exception NotFoundException Если в базе нет отчёта с указанным id
     */
    void delete(Report report);

    /**
     * Удаление отчёта по id<br>
     * Используется метод этого же сервиса {@link ReportServiceImpl#getById(Long)}
     * @param id id отчёта
     * @exception NotFoundException Если в базе нет отчёта с указанным id
     */
    void deleteById(Long id);

    /**
     * Создание отчёта по данным из телеграма<br>
     * Используются методы из сервиса испытательных сроков {@link TrialPeriodService#getAllByOwnerId(Long)} и
     * {@link TrialPeriodService#update(TrialPeriod)}
     * Так же методы этого же сервиса {@link ReportService#create(Report)}
     * @param photoId Id фотографии из чата в телеграме
     * @param caption Описание под фотографией
     * @param id id пользователя
     * @exception IllegalArgumentException Если описание пустое или равно null, а так же, если формат данных не совпадает с regEx
     * @exception AlreadyExistsException Если отчёт уже отправляли в этот день
     */
    void createFromTelegram(String photoId, String caption, Long id);
}
