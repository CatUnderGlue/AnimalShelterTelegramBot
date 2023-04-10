package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;

import java.util.List;

public interface TrialPeriodService {
    /**
     * Сохранение испытательного периода в бд (Он же отвечает за обновление уже существующего отчёта)
     * @param trialPeriod Испытательный срок для сохранения в бд
     * @return id испытательного срока
     */
    Long create(TrialPeriod trialPeriod);

    /**
     * Получение испытательного срока из бд по id
     * @param id id испытательного срока
     * @return Испытательный срок
     * @exception NotFoundException Если в базе нет испытательного срока с указанным id
     */
    TrialPeriod getById(Long id);

    /**
     * Получение всех отчётов
     * @return Список всех отчётов
     * @exception NotFoundException Если база с испытательными сроками пустая
     */
    List<TrialPeriod> getAll();

    /**
     * Получение всех отчётов конкретного пользователя
     * @param ownerId id хозяина, по которому будет идти поиск
     * @return Список испытательных сроков по хозяину
     * @exception NotFoundException Если у пользователя отсутствуют испытательные сроки
     */
    List<TrialPeriod> getAllByOwnerId(Long ownerId);

    /**
     * Изменение существующего испытательного срока
     * @param trialPeriod Испытательный срок
     * @return Обновлённый испытательный срок
     * @exception NotFoundException Если у передаваемого испытательного срока нет id или в базе нет испытательного срока с указанным id
     */
    TrialPeriod update(TrialPeriod trialPeriod);

    /**
     * Удаление полученного из бд испытательного срока
     * @param trialPeriod Испытательный срок, который уже есть в бд
     * @exception NotFoundException Если в базе нет испытательного срока с указанным id
     */
    void delete(TrialPeriod trialPeriod);

    /**
     * Удаление испытательного срока по id
     * @param id id испытательного срока
     * @exception NotFoundException Если в базе нет испытательного срока с указанным id
     */
    void deleteById(Long id);
}
